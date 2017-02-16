package com.timss.asset.util;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import com.timss.asset.bean.AstOrganizeData;
import com.timss.inventory.vo.OrganizeDataAttrVO;
import com.yudean.itc.util.ResourceLoader;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: ImportUtil.java
 * @author: 890166
 * @createDate: 2015-6-1
 * @updateUser: 890166
 * @version: 1.0
 */
public class ImportUtil {

    private ImportUtil() {

    }

    private static final Logger LOG = Logger.getLogger( ImportUtil.class );

    /**
     * @description: 读取初始化excel配置文件
     * @author: 890166
     * @createDate: 2015-6-1
     * @param sortOrder
     * @return:
     */
    private static List<OrganizeDataAttrVO> readExcelConfigXML() {
        OrganizeDataAttrVO odav = null;
        List<OrganizeDataAttrVO> odavList = new ArrayList<OrganizeDataAttrVO>();
        try {
            Set<Resource> resSet = ResourceLoader.getResources( "/import/excel/astinitdata.xml" );
            if ( !resSet.isEmpty() ) {
                for ( Resource res : resSet ) {
                    // 流文件形式读取
                    InputStream in = res.getInputStream();
                    // 转换成dom4j里面的document对象
                    SAXReader reader = new SAXReader();
                    Document doc = reader.read( in );
                    // 通过传入参数找到匹配节点
                    List<Element> elemList = doc.selectNodes( "excel/cell" );
                    // 若节点存在
                    if ( !elemList.isEmpty() ) {
                        for ( Element elem : elemList ) {
                            odav = new OrganizeDataAttrVO();
                            // 获取节点内的所有属性
                            List<Attribute> attrList = elem.attributes();
                            for ( Attribute attr : attrList ) {
                                setFieldValueByFieldName( odav, attr.getName(), attr.getValue() );
                            }
                            odavList.add( odav );
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException( "---------ImportUtil 中的 readExcelConfigXML 方法抛出异常---------：", e );
        }
        return odavList;
    }

    /**
     * @description: 结合xml验证Excel数据有效性
     * @author: 890166
     * @createDate: 2015-6-1
     * @return:
     */
    private static Map<String, String> validateExcelData(OrganizeDataAttrVO odav, String cellValue, int row, int column) {
        Map<String, String> reMap = new HashMap<String, String>();
        // 返回提示信息
        StringBuilder remarkSB = new StringBuilder();
        String cName = odav.getcName();
        // 验证不为空
        if ( "N".equals( odav.getIsNull() ) && (null == cellValue || "".equals( cellValue )) ) {
            String isNullExcp = getProperties( "isNullExcp" ).replace( "{row}", String.valueOf( row ) )
                    .replace( "{column}", String.valueOf( column ) ).replace( "{fieldName}", cName );
            remarkSB.append( isNullExcp );
        }
        // 验证字段长度是否超出设定长度
        if ( null != odav.getMaxLength() ) {
            String maxLengthExcp = null;
            int maxLength = Integer.valueOf( odav.getMaxLength() ) / 3;
            try {
                if ( cellValue.length() > maxLength ) {
                    maxLengthExcp = getProperties( "maxLengthExcp" ).replace( "{row}", String.valueOf( row ) )
                            .replace( "{column}", String.valueOf( column ) ).replace( "{fieldName}", cName )
                            .replace( "{lengthValue}", String.valueOf( maxLength ) );
                    remarkSB.append( maxLengthExcp );
                    // 若字段过长将截取字符串并用省略号表示后面的值
                    reMap.put( "maxLengthValue", cellValue.substring( 0, maxLength - 3 ) + "..." );
                }
            } catch (Exception e) {
                maxLengthExcp = getProperties( "maxLengthExcpS" ).replace( "{row}", String.valueOf( row ) )
                        .replace( "{column}", String.valueOf( column ) ).replace( "{fieldName}", cName );
                remarkSB.append( maxLengthExcp );
                // 若字段过长将截取字符串并用省略号表示后面的值
                reMap.put( "maxLengthValue", cellValue.substring( 0, maxLength ) + "..." );
            }

        }
        // 如果类型不是String，而且不为空的时候可以进行类型转换测试
        if ( null != odav.getType() && !"String".equals( odav.getType() ) ) {
            String typeExcp = null;
            try {
                if ( "Number".equals( odav.getType() ) ) {
                    BigDecimal value = new BigDecimal( cellValue );
                    LOG.info( cellValue + "can be tranlated to BigDecimal! value=" + value );
                } else if ( "Date".equals( odav.getType() ) ) {
                    DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                    Date value = format.parse( cellValue );
                    LOG.info( cellValue + "can be tranlated to Date! value=" + value );
                }
            } catch (Exception e) {
                typeExcp = getProperties( "typeExcp" ).replace( "{row}", String.valueOf( row ) )
                        .replace( "{column}", String.valueOf( column ) ).replace( "{fieldName}", cName )
                        .replace( "{type}", odav.getType() );
                remarkSB.append( typeExcp );
            }
        }
        // 最后就两个返回状态一个 false 一个success
        if ( !"".equals( remarkSB ) ) {
            reMap.put( "false", remarkSB.toString() );
        } else {
            reMap.put( "success", null );
        }
        return reMap;
    }

    /**
     * @description: 将Excel中的数据放入到实体类里面
     * @author: 890166
     * @createDate: 2015-6-1
     * @param iod
     * @param odav
     * @return:
     */
    private static AstOrganizeData setOrganizeData2Entity(AstOrganizeData aod, OrganizeDataAttrVO odav,
            String cellValue, int row, int column) throws Exception {

        // 若有错误输出，则将错误原因和状态设置到“错误备注”和“状态”字段中
        Map<String, String> validMap = validateExcelData( odav, cellValue, row, column );
        String remarkNew = String.valueOf( validMap.get( "false" ) );
        String maxLengthValue = validMap.get( "maxLengthValue" ) == null ? "" : String.valueOf( validMap
                .get( "maxLengthValue" ) );
        if ( null != remarkNew && !"".equals( remarkNew ) ) {
            if ( null != aod.getRemark() ) {
                remarkNew = aod.getRemark() + remarkNew;
            }
            aod.setRemark( remarkNew );
            aod.setStatus( "1" );
        }
        // 若验证的时候出现超过长度的情况，将截取字符串后重新设值
        if ( !"".equals( maxLengthValue ) ) {
            cellValue = maxLengthValue;
        }
        // 将值设入到实体类中
        setFieldValueByFieldName( aod, odav.getName(), cellValue );
        return aod;
    }

    /**
     * @description: 全部字段都转换成字符串
     * @author: 890166
     * @createDate: 2015-6-2
     * @param cell
     * @return:
     */
    private static String getStringCellValue(HSSFCell cell) {
        String strCell = null;
        if ( null == cell ) {
            strCell = "";
        } else {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    strCell = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    strCell = String.valueOf( cell.getNumericCellValue() );
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    strCell = String.valueOf( cell.getBooleanCellValue() );
                    break;
                case Cell.CELL_TYPE_BLANK:
                    strCell = "";
                    break;
                default:
                    strCell = "";
                    break;
            }
        }

        if ( "".equals( strCell ) || null == strCell ) {
            return "";
        }
        return strCell;
    }

    /**
     * @description:读取excel没数据将其转换成List的形式输出
     * @author: 890166
     * @createDate: 2015-6-1
     * @param is
     * @return:
     */
    public static List<AstOrganizeData> readAndChange2List(InputStream is) throws Exception {
        // 获取配置文件
        List<OrganizeDataAttrVO> odavList = readExcelConfigXML();

        List<AstOrganizeData> aodList = new ArrayList<AstOrganizeData>();

        // 引入POI解析流文件
        POIFSFileSystem fs = new POIFSFileSystem( is );
        HSSFWorkbook wb = new HSSFWorkbook( fs );
        HSSFSheet sheet = wb.getSheetAt( 0 );
        int rowNum = sheet.getLastRowNum();

        HSSFRow row = sheet.getRow( 0 );
        int colNum = odavList.size();

        HSSFCell cell = null;
        AstOrganizeData aod = null;
        String cellStr = null;
        // 通过遍历行，获取行数据
        for ( int i = 1; i <= rowNum; i++ ) {
            aod = new AstOrganizeData();
            row = sheet.getRow( i );
            // 一行中再做遍历，得到每个单元格的数据
            for ( int j = 0; j < colNum; j++ ) {
                cell = row.getCell( (short) j );
                cellStr = getStringCellValue( cell ).trim();
                OrganizeDataAttrVO odav = odavList.get( j );
                if ( j <= odavList.size() - 1 ) {
                    setOrganizeData2Entity( aod, odav, cellStr, i + 1, j + 1 );
                }
            }
            aodList.add( aod );
        }
        return aodList;
    }

    /**
     * @description:通过反射设置字段中存值
     * @author: 890166
     * @createDate: 2015-6-1
     * @param obj
     * @param fieldName
     * @return
     * @throws Exception :
     */
    private static Object setFieldValueByFieldName(Object obj, String fieldName, String fieldValue) throws Exception {
        Class<?> objClass = obj.getClass();
        fieldValue = fieldValue.trim();
        // 获取字段信息
        Field field = objClass.getDeclaredField( fieldName );
        field.setAccessible( true );

        // 根据设入的实体类字段类型判断
        if ( "java.lang.String".equals( field.getType().getName() ) ) {// 转换成字符串
            String value = fieldValue;
            field.set( obj, value );
        } else if ( "java.util.Date".equals( field.getType().getName() ) ) {// 转换成日期
            SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            String dateStr = sdf.format( sdf.parse( fieldValue ) );

            DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            Date value = format.parse( dateStr );
            field.set( obj, value );
        } else if ( "java.math.BigDecimal".equals( field.getType().getName() ) ) {// 转换成大浮点
            String bigDecimalStr = fieldValue;
            BigDecimal value = new BigDecimal( bigDecimalStr );
            field.set( obj, value );
        } else if ( "java.lang.Double".equals( field.getType().getName() ) ) {// 转换成double
            String doubleStr = fieldValue;
            Double value = Double.valueOf( doubleStr );
            field.set( obj, value );
        } else if ( "java.lang.Integer".equals( field.getType().getName() ) ) {// 转换成double
            String integerStr = fieldValue;
            Integer value = Integer.valueOf( integerStr );
            field.set( obj, value );
        }
        return obj;
    }

    /**
     * @description:获取properties文件
     * @author: 890166
     * @createDate: 2014-8-16
     * @param key
     * @return:
     */
    private static String getProperties(String key) {
        Properties p = null;
        InputStream in = null;
        String reStr = null;
        try {
            in = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream( "/import/excel/astinitdata.properties" );
            p = new Properties();
            p.load( in );
            reStr = p.getProperty( key );
            reStr = new String( reStr.getBytes( "ISO-8859-1" ), "UTF-8" );
        } catch (Exception e) {
            throw new RuntimeException( "---------getProperties 方法抛出异常---------：", e );
        } finally {
            try {
                if ( in != null ) {
                    in.close();
                }
            } catch (Exception e) {
                throw new RuntimeException( "---------getProperties 方法抛出异常---------：", e );
            }
        }
        return reStr;
    }
}
