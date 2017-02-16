package com.timss.operation.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;

import com.yudean.mvc.bean.userinfo.UserInfoScope;


public abstract class ReflectionUtils {

    private static final Logger log = Logger.getLogger(ReflectionUtils.class);
    
    public static Object setCommonFieldValue(Object obja, Object objb,String excludeFields) {
        //获取可访问的属性
            Field[] fieldas = getAllFields(obja);
            Field[] fieldbs = getAllFields(objb);
            for (Field fielda : fieldas) {
                    String fieldaName = fielda.getName();
                    Class<?> aclass = fielda.getType();
                    if(excludeFields!=null && excludeFields.indexOf(fieldaName) < 0){
                            for (Field fieldb : fieldbs) {
                                           Class<?> bclass = fieldb.getType();
                                            String fieldbName = fieldb.getName();
                                           if(bclass.equals(aclass) && fieldaName.equals(fieldbName)){  //如果两个属性名字和类型一样，则赋值
                                                   Object fieldValue = obtainFieldValue(obja,fieldaName);
                                                   setFieldValue(objb,fieldaName,fieldValue);
                                           }
                                   }
                    }
           }
            return objb;
    }
    
    public static Field[] getAllFields(Object obj){
                   Field[] fields = new Field[]{};
                   for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
                                   .getSuperclass()) {
                           fields = (Field[]) ArrayUtils.addAll(fields, clazz.getDeclaredFields());
                   }
                   return fields;
   }
    /**
        * 直接读取对象属性值
        * 忽视private/protected修饰符，不经过getter函数
        *
        * @param obj
        * @param fieldName
        * @return
        */
       public static Object obtainFieldValue(final Object obj, final String fieldName) {
           Field field = obtainAccessibleField(obj, fieldName);
           if (field == null) {
               throw new IllegalArgumentException("Devkit: could not find field [" + fieldName + "] on target [" + obj + "]");
           }
           Object retval = null;
           
           try {
                           retval = field.get(obj);
                   } catch (IllegalArgumentException e) {
                           e.printStackTrace();
                   } catch (IllegalAccessException e) {
                           e.printStackTrace();
                   }
           
           return retval;

       }
       
       
    /**
        * 直接设置对象属性值
        * 忽视private/protected修饰符，不经过setter函数
        *
        * @param obj
        * @param fieldName
        * @param value
        */
       public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
           Field field = obtainAccessibleField(obj, fieldName);
           if (field == null) {
               throw new IllegalArgumentException("Devkit: could not find field [" + fieldName + "] on target [" + obj + "]");
           }
          
            try {
                           field.set(obj, value);
                   } catch (IllegalArgumentException e) {
                           e.printStackTrace();
                   } catch (IllegalAccessException e) {
                           e.printStackTrace();
                   }
           
       }
       
       /**
        * 循环向上转型，获取对象的DeclaredField,并强制设为可访问
        * 如向上转型Object仍无法找到，返回null
        *
        * @param obj
        * @param fieldName
        * @return
        */
       public static Field obtainAccessibleField(final Object obj,
                                                 final String fieldName) {
           Class<?> superClass = obj.getClass();
           Class<Object> objClass = Object.class;
           for (; superClass != objClass; superClass = superClass.getSuperclass()) {
               
                   Field field;
                   try {
                           field = superClass.getDeclaredField(fieldName);
                           field.setAccessible(true);
                           return field;
                       } catch (NoSuchFieldException e) {
                           log.error( e );
                       } catch (SecurityException e) {
                           log.error( e );
                       }
           }
           return null;
       }
       
       /**
     * @description:设置timss中bean的公共属性值
     * @author: 王中华
     * @createDate: 2016-12-5
     * @param obj
     * @param userInfo
     * @return
     * @throws Exception:
     */
    public static Object setTimssCommonFieldValue(Object obj,UserInfoScope userInfo) throws Exception{
           Class<?> objClass = obj.getClass();
           Method m1 = null;
           Method m2 = null;
           Method m3 = null;
           Method m4 = null;
           Method m5 = null;
           Method m6 = null;
           
           m1 = objClass.getMethod("setCreateuser",String.class);
           m1.invoke(obj,userInfo.getUserId());
           m2 = objClass.getMethod("setCreatedate",Date.class);
           m2.invoke(obj,new Date());
           m3 = objClass.getMethod("setModifyuser",String.class);
           m3.invoke(obj,userInfo.getUserId());
           m4 = objClass.getMethod("setModifydate",Date.class);
           m4.invoke(obj,new Date());
           m5 = objClass.getMethod("setSiteid",String.class);
           m5.invoke(obj,userInfo.getSiteId());
           m6 = objClass.getMethod("setDelFlag",String.class);
           m6.invoke(obj,"N");
           
           
           return obj;
       }
       

}
