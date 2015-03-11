package net.dateutils;

/*
 * Copyright (C) 2015 Martin Wolf
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of  MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import jolie.runtime.FaultException;
import jolie.runtime.JavaService;
import jolie.runtime.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils extends JavaService {

    private int getJavaMonth(int month) throws FaultException {
        int result;
        switch (month) {
            case 1:
                result = Calendar.JANUARY;
                break;
            case 2:
                result = Calendar.FEBRUARY;
                break;
            case 3:
                result = Calendar.MARCH;
                break;
            case 4:
                result = Calendar.APRIL;
                break;
            case 5:
                result = Calendar.MAY;
                break;
            case 6:
                result = Calendar.JUNE;
                break;
            case 7:
                result = Calendar.JULY;
                break;
            case 8:
                result = Calendar.AUGUST;
                break;
            case 9:
                result = Calendar.SEPTEMBER;
                break;
            case 10:
                result = Calendar.OCTOBER;
                break;
            case 11:
                result = Calendar.NOVEMBER;
                break;
            case 12:
                result = Calendar.DECEMBER;
                break;
            default:
                throw new FaultException("WrongMonthNumber", "The month number " + month + " does not exist.");
        }

        return result;
    }

    @SuppressWarnings("MagicConstant")
    private Date dateNodeToDateObject(Value dateNode) throws FaultException {
        Calendar cal = Calendar.getInstance();

        int hour = dateNode.getFirstChild("hour").intValue();
        int minute = dateNode.getFirstChild("minute").intValue();
        int second = dateNode.getFirstChild("second").intValue();
        int day = dateNode.getFirstChild("day").intValue();
        int month = dateNode.getFirstChild("month").intValue();
        int year = dateNode.getFirstChild("year").intValue();

        cal.set(year, getJavaMonth(month), day, hour, minute, second);
        return cal.getTime();
    }

    /**
     * Parse a date string
     *
     * @param dateValue
     * @return value with parsed info
     */
    public Value parse(Value dateValue) throws FaultException {
        String dateString = dateValue.strValue();
        String format = dateValue.getFirstChild("format").strValue();

        Date parsedDate;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatParser = new SimpleDateFormat(format);
        try {
            parsedDate = dateFormatParser.parse(dateString);
            cal.setTime(parsedDate);
        } catch (ParseException e) {
            throw new FaultException("ParsingError", "Could not parse " + dateString);
        }

        Value result = Value.create();
        result.getNewChild("hour").setValue(cal.get(Calendar.HOUR));
        result.getNewChild("minute").setValue(cal.get(Calendar.MINUTE));
        result.getNewChild("second").setValue(cal.get(Calendar.SECOND));
        result.getNewChild("day").setValue(cal.get(Calendar.DAY_OF_MONTH));
        result.getNewChild("month").setValue(cal.get(Calendar.MONTH) + 1);
        result.getNewChild("year").setValue(cal.get(Calendar.YEAR));
        return result;
    }

    public Value isBefore(Value dateValues) throws FaultException {
        Date before = dateNodeToDateObject(dateValues.getFirstChild("before"));
        Date after = dateNodeToDateObject(dateValues.getFirstChild("after"));

        return Value.create(before.before(after));
    }
}
