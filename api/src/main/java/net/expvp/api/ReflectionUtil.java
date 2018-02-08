package net.expvp.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

/**
 * Class for handling all reflection handling exceptions
 * 
 * @author NullUser
 */
public final class ReflectionUtil {

	public final static String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

	/**
	 * Invokes selected method with selected types
	 * 
	 * @param method
	 *            To invoke
	 * @param o1
	 *            To invoke from
	 * @param types
	 *            To invoke with
	 * @return returning object from invocation
	 */
	public static Object invoke(Method method, Object o1, Object... types) {
		try {
			return method.invoke(o1, types);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the object from the field of the object
	 * 
	 * @param field
	 *            To get the object from
	 * @param o1
	 *            To get the field from
	 * @return the field data
	 */
	public static Object get(Field field, Object o1) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
			return field.get(o1);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Sets the field data
	 * 
	 * @param field
	 *            To set the object from
	 * @param o1
	 *            To set the object from the field to
	 * @param o2
	 *            To set the field's data
	 */
	public static void setField(Field field, Object o1, Object o2) {
		try {
			if (!field.isAccessible()) {
				field.setAccessible(false);
			}
			field.set(o1, o2);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the field from a class
	 * 
	 * @param clazz
	 *            To get the field from
	 * @param field
	 *            The field name
	 * @return the received field
	 */
	public static Field getField(Class<?> clazz, String field) {
		Field f;
		try {
			f = clazz.getDeclaredField(field);
			if (f == null) {
				f = clazz.getField(field);
			}
			return f;
		} catch (Exception e1) {
			try {
				f = clazz.getField(field);
				return f;
			} catch (Exception e2) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	/**
	 * Gets the method from a class
	 * 
	 * @param clazz
	 *            To get the field from
	 * @param method
	 *            The method name
	 * @param types
	 *            The method invocation types
	 * @return the received method
	 */
	public static Method getMethod(Class<?> clazz, String method, Class<?>... types) {
		Method m;
		try {
			m = clazz.getDeclaredMethod(method, types);
			if (m == null) {
				m = clazz.getMethod(method, types);
			}
			return m;
		} catch (Exception e1) {
			try {
				m = clazz.getMethod(method, types);
				return m;
			} catch (Exception e2) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... types) {
		try {
			return clazz.getConstructor(types);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the class from craftbukkit
	 * 
	 * @param clazz
	 *            The name of the class
	 * @return the received class
	 */
	public static Class<?> getCBClass(String clazz) {
		return getClass("org.bukkit.craftbukkit." + VERSION + "." + clazz);
	}

	/**
	 * Gets the class from nms
	 * 
	 * @param clazz
	 *            The name of the class
	 * @return the received class
	 */
	public static Class<?> getNMSClass(String clazz) {
		return getClass("net.minecraft.server." + VERSION + "." + clazz);
	}

	/**
	 * Gets a class
	 * 
	 * @param clazz
	 *            The name of the class
	 * @return the received class
	 */
	public static Class<?> getClass(String clazz) {
		try {
			return Class.forName(clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets a new instance from a constructor
	 * 
	 * @param cons
	 *            The constructor
	 * @param types
	 *            The object types
	 * @return the new object
	 */
	public static Object newInstance(Constructor<?> cons, Object... types) {
		try {
			return cons.newInstance(types);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
