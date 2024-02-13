package csv;

import java.time.LocalTime;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class LocalTimeConverter extends AbstractBeanField<Object, Object> {

	@Override
	protected Object convert(String arg0) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
		if (arg0 != "")
			return LocalTime.parse(arg0);
		return LocalTime.parse("00:00");
	}

}
