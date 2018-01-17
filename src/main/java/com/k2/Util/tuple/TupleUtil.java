package com.k2.Util.tuple;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;

import com.k2.Util.exceptions.UtilityError;

/**
 * This class provides static utility methods for creating and dealing with Tuples and the concrete implementations of the Tuple interface
 * 
 * @author simon
 *
 */
public class TupleUtil {

	/**
	 * This static method creates an instance of Tuple with the given values aliased by the given array of aliases
	 * 
	 * @param aliases	The aliases to use for members of the Tuple
	 * @param values		The values to set as members of the Tuple
	 * @return			The Tuple implementation for the given values identified by the given aliases
	 */
	public static Tuple aliasedTuple(String [] aliases, Object ...values) {
		if (values != null && values.length > 26) return new GenericTuple(aliases, values);
		Tuple tuple = tuple(values);
		((AbstractTuple)tuple).setAliases(aliases);
		return tuple;
	}
	
	/**
	 * This static method creates an instance of Tuple with the given values aliased by the aliases "a", "b", "c" etc.
	 * 
	 * @param values		The values to set as members of the Tuple
	 * @return			The Tuple implementation for the given values
	 */
	@SuppressWarnings("rawtypes")
	public static Tuple tuple(Object ...values) {
		if (values == null) throw new UtilityError("Unable to craete Tuple with a null values array");
		if (values.length == 0) throw new UtilityError("Unable to craete Tuple with an empty values array");
		switch(values.length) {
		case 1:
			return new Tuple1(values);
		case 2:
			return new Tuple2(values);
		case 3:
			return new Tuple3(values);
		case 4:
			return new Tuple4(values);
		case 5:
			return new Tuple5(values);
		case 6:
			return new Tuple6(values);
		case 7:
			return new Tuple7(values);
		case 8:
			return new Tuple8(values);
		case 9:
			return new Tuple9(values);
		case 10:
			return new Tuple10(values);
		case 11:
			return new Tuple11(values);
		case 12:
			return new Tuple12(values);
		case 13:
			return new Tuple13(values);
		case 14:
			return new Tuple14(values);
		case 15:
			return new Tuple15(values);
		case 16:
			return new Tuple16(values);
		case 17:
			return new Tuple17(values);
		case 18:
			return new Tuple18(values);
		case 19:
			return new Tuple19(values);
		case 20:
			return new Tuple20(values);
		case 21:
			return new Tuple21(values);
		case 22:
			return new Tuple22(values);
		case 23:
			return new Tuple23(values);
		case 24:
			return new Tuple24(values);
		case 25:
			return new Tuple25(values);
		case 26:
			return new Tuple26(values);
		default:
			return new GenericTuple(values);
		}
	}
	
	/**
	 * This static method creates an instance of TupleElement for the given alias and class
	 * 
	 * @param alias	The alias of the TupleElement
	 * @param cls	The class of the TupleElement
	 * @param <T> 	The type of the member of the Tuple referenced by this TupleElement
	 * @return		The TupleElement for the given alias and Element
	 */
	public static <T> TupleElement<T> tupleElement(String alias, Class<? extends T> cls) {
		return new TupleElementImpl<T>(alias, cls);
	}

	/**
	 * This static method creates an instance of TupleElement for the given class only. Such TupleElements will match the first member of the
	 * given class
	 * 
	 * @param cls	The class of the TupleElement
	 * @param <T>	The type of the member of the Tuple referenced by this TupleElement
	 * @return		The TupleElement for the given class
	 */
	public static <T> TupleElement<T> tupleElement(Class<? extends T> cls) {
		return new TupleElementImpl<T>(cls);
	}

}
