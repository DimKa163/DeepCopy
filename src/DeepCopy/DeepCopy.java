package DeepCopy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class DeepCopy<TValue>
{
    private  TValue value;
    private Map<Object, Object> originalObjects;

    public DeepCopy(TValue value){
        this.value = value;
        this.originalObjects = new HashMap<Object, Object>();
    }

    public DeepCopy(TValue value, Map<Object, Object> originalObjects){
        this.value = value;
        this.originalObjects = originalObjects;
    }

    public TValue cloneObject(){
        if (value == null){
            return null;
        }
        Class cls = value.getClass();
        if (cls.isArray()){
            Object[] array = ((Object[]) value).clone();
            for (int i = 0; i < array.length; i++) {
                array[i] = DeepCopy.clone(array[i]);
            }
            TValue newValue = (TValue)array;
            originalObjects.put(value, newValue);
            return newValue;
        }
        if (value instanceof String) {
            TValue newValue = (TValue) new String((String)value);
            originalObjects.put(value, newValue);
            return newValue;
        } else if (value instanceof Integer) {
            return (TValue) new Integer((int)value);
        } else if (value instanceof Long) {
            return (TValue) new Long((long)value);
        } else if (value instanceof Double) {
            return (TValue) new Double((double)value);
        } else if (value instanceof Float) {
            return (TValue) new Float((float)value);
        } else if (value instanceof Short) {
            return (TValue) new Short((short)value);
        } else if (value instanceof Byte) {
            return (TValue) new Byte((byte)value);
        } else if (value instanceof Character) {
            return (TValue) new Character((char)value);
        } else if (value instanceof Boolean) {
            return (TValue) new Boolean((boolean)value);
        }else if (value instanceof List){
            List src = (List)value;
            List newItem = (List)createInstance(cls);
            for (int i = 0; i < src.size(); i++) {
                newItem.add(DeepCopy.clone(src.get(i)));
            }
            originalObjects.put(value, newItem);
            return (TValue)newItem;
        }
        TValue newValue;
        Constructor[] constructors = cls.getConstructors();
        if (constructors.length == 0)
            newValue = createInstance(cls, null);
        else
            newValue = createInstance(cls, constructors[0]);
        originalObjects.put(value, newValue);
        while(cls != null){
            copy(newValue, cls);
            cls = cls.getSuperclass();
        }
        return newValue;
    }

    private void copy(TValue newValue, Class newCls){
        Field[] fields = newCls.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try{
                Object fieldValue = field.get(value);
                Object v = originalObjects.get(fieldValue);
                if(v != null)
                    field.set(newValue, v);
                else{
                    Object value = DeepCopy.clone(fieldValue, originalObjects);
                    field.set(newValue, value);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private TValue createInstance(Class cls){
        TValue value;
        try{
            value = (TValue) cls.newInstance();
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return value;
    }

    private TValue createInstance(Class cls, Constructor constructor){
        TValue newValue;
        if (constructor == null)
            return  createInstance(cls);
        Class[] parameters = constructor.getParameterTypes();
        Object[] parametersDefValues = new Object[parameters.length];
        for(int i = 0; i < parametersDefValues.length; i++)
            parametersDefValues[i] = getDefaultValues(parameters[i]);
        try{
            newValue = (TValue) constructor.newInstance(parametersDefValues);
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        return newValue;
    }

    private Object getDefaultValues(Class cls){
        if (cls == String.class) {
            return "";
        } else if (cls == int.class) {
            return 0;
        } else if (cls ==  Long.class) {
            return 0;
        } else if (cls ==  Double.class) {
            return 0.0;
        } else if (cls ==  Float.class) {
            return 0.0;
        } else if (cls ==  Short.class) {
            return 0;
        } else if (cls ==  Byte.class) {
            return 0;
        } else if (cls ==  Character.class) {
            return ' ';
        }else{
            return null;
        }
    }


    public static <TValue> TValue clone(TValue object){
        DeepCopy<TValue> copyWorker = new DeepCopy<TValue>(object);
        return copyWorker.cloneObject();
    }
    public static <TValue> TValue clone(TValue object, Map<Object, Object> objects){
        DeepCopy<TValue> copyWorker = new DeepCopy<TValue>(object, objects);
        return copyWorker.cloneObject();
    }
}
