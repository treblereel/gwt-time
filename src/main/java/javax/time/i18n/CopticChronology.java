/*
 * Copyright (c) 2007-2011, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.i18n;

import java.io.Serializable;

import javax.time.Duration;
import javax.time.calendar.Calendrical;
import javax.time.calendar.CalendricalMerger;
import javax.time.calendar.CalendricalRule;
import javax.time.calendar.Chronology;
import javax.time.calendar.DateTimeField;
import javax.time.calendar.DateTimeRule;
import javax.time.calendar.DateTimeRuleRange;
import javax.time.calendar.DayOfWeek;
import javax.time.calendar.ISOPeriodUnit;
import javax.time.calendar.PeriodUnit;

/**
 * The Coptic calendar system.
 * <p>
 * CopticChronology defines the rules of the Coptic calendar system.
 * The Coptic calendar has twelve months of 30 days followed by an additional
 * period of 5 or 6 days, modelled as the thirteenth month in this implementation.
 * <p>
 * Years are measured in the 'Era of the Martyrs'.
 * 0001-01-01 (Coptic) equals 0284-08-29 (ISO).
 * The supported range is from Coptic year 1 to year 9999 (inclusive).
 * <p>
 * CopticChronology is immutable and thread-safe.
 *
 * @author Stephen Colebourne
 */
public final class CopticChronology extends Chronology implements Serializable {

    /**
     * The singleton instance of {@code CopticChronology}.
     */
    public static final CopticChronology INSTANCE = new CopticChronology();
    /**
     * A serialization identifier for this class.
     */
    private static final long serialVersionUID = 1L;

    //-----------------------------------------------------------------------
    /**
     * Restrictive constructor.
     */
    private CopticChronology() {
    }

    /**
     * Resolves singleton.
     *
     * @return the singleton instance
     */
    private Object readResolve() {
        return INSTANCE;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if the specified year is a leap year.
     * <p>
     * A year is leap if the remainder after division by four equals three.
     * This method does not validate the year passed in, and only has a
     * well-defined result for years in the supported range.
     *
     * @param year  the year to check, not validated for range
     * @return true if the year is a leap year
     */
    public static boolean isLeapYear(long year) {
        return ((year % 4) == 3);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the name of the chronology.
     *
     * @return the name of the chronology, not null
     */
    @Override
    public String getName() {
        return "Coptic";
    }

//    //-----------------------------------------------------------------------
//    /**
//     * Gets the equivalent rule for the specified field in the Coptic chronology.
//     * <p>
//     * This will take the input field and provide the closest matching field
//     * that is based......
//     *
//     * @param rule  the rule to convert, not null
//     * @return the rule in Coptic chronology, not null
//     */
////    @Override
//    public DateTimeRule<?> convertRule(DateTimeRule<?> rule) {
//        if (rule.getChronology().equals(this)) {
//            return rule;
//        }
//        rule = convertToISO(rule);
//        if (rule.equals(ISODateTimeRule.YEAR)) {
//            return year();
//        }
//        if (rule.equals(ISODateTimeRule.MONTH_OF_YEAR)) {
//            return monthOfYear();
//        }
//        if (rule.equals(ISODateTimeRule.DAY_OF_MONTH)) {
//            return dayOfMonth();
//        }
//        if (rule.equals(ISODateTimeRule.DAY_OF_YEAR)) {
//            return dayOfYear();
//        }
//        if (rule.equals(ISODateTimeRule.DAY_OF_WEEK)) {
//            return dayOfWeek();
//        }
//        return null;
//    }
//
//    /**
//     * Gets the equivalent rule for the specified field in the Coptic chronology.
//     * <p>
//     * This will take the input field and provide the closest matching field
//     * that is based......
//     *
//     * @param rule  the rule to convert, not null
//     * @return the rule in ISO chronology, not null
//     */
////    @Override
//    protected DateTimeRule<?> convertToISO(DateTimeRule<?> rule) {
//        if (rule.getChronology().equals(ISOChronology.INSTANCE)) {
//            return rule;
//        }
//        if (rule.equals(year())) {
//            return ISODateTimeRule.YEAR;
//        }
//        if (rule.equals(monthOfYear())) {
//            return ISODateTimeRule.MONTH_OF_YEAR;
//        }
//        if (rule.equals(dayOfMonth())) {
//            return ISODateTimeRule.DAY_OF_MONTH;
//        }
//        if (rule.equals(dayOfYear())) {
//            return ISODateTimeRule.DAY_OF_YEAR;
//        }
//        if (rule.equals(dayOfWeek())) {
//            return ISODateTimeRule.DAY_OF_WEEK;
//        }
//        return null;
//    }

    //-----------------------------------------------------------------------
    /**
     * Merges the fields.
     * 
     * @param merger  the merge context
     */
    static void merge(CalendricalMerger merger) {
        DateTimeField year = merger.getValue(CopticChronology.YEAR);
        if (year != null) {
            // year-month-day
            DateTimeField moy = merger.getValue(CopticChronology.MONTH_OF_YEAR);
            DateTimeField dom = merger.getValue(CopticChronology.DAY_OF_MONTH);
            if (moy != null && dom != null) {
                CopticDate date = CopticDate.of(year.getValidIntValue(), moy.getValidIntValue(), dom.getValidIntValue());
                merger.storeMerged(CopticDate.rule(), date);
                merger.removeProcessed(CopticChronology.YEAR);
                merger.removeProcessed(CopticChronology.MONTH_OF_YEAR);
                merger.removeProcessed(CopticChronology.DAY_OF_MONTH);
            }
            // year-day
            DateTimeField doy = merger.getValue(CopticChronology.DAY_OF_YEAR);
            if (doy != null) {
                CopticDate date = CopticDate.of(year.getValidIntValue(), 1, 1).withDayOfYear(doy.getValidIntValue());
                merger.storeMerged(CopticDate.rule(), date);
                merger.removeProcessed(CopticChronology.YEAR);
                merger.removeProcessed(CopticChronology.DAY_OF_YEAR);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * The period unit for days in the Coptic calendar system.
     * <p>
     * This is equivalent to the ISO days period unit.
     */
    public static final PeriodUnit DAYS = ISOPeriodUnit.DAYS;

    /**
     * The period unit for weeks in the Coptic calendar system.
     * <p>
     * This is equivalent to the ISO weeks period unit.
     */
    public static final PeriodUnit WEEKS = ISOPeriodUnit.WEEKS;

    /**
     * The period unit for months in the Coptic calendar system.
     * <p>
     * This is a basic unit and has no equivalent period.
     * The estimated duration is equal to 30 days.
     * This unit does not depend on any other unit.
     */
    public static final PeriodUnit MONTHS = new Months();
    /** Unit class for months. */
    private static final class Months extends PeriodUnit implements Serializable {
        private static final long serialVersionUID = 1L;
        private Months() {
            super("CopticMonths", Duration.ofSeconds(30L * 86400));
        }
        private Object readResolve() {
            return MONTHS;
        }
    }

    /**
     * The period unit for years in the Coptic calendar system.
     * <p>
     * This is a basic unit and has no equivalent period.
     * The estimated duration is equal to 365.25 days.
     * This unit does not depend on any other unit.
     */
    public static final PeriodUnit YEARS = new Years();
    /** Unit class for years. */
    private static final class Years extends PeriodUnit implements Serializable {
        private static final long serialVersionUID = 1L;
        private Years() {
            super("CopticYears", Duration.ofSeconds(31557600L));  // 365.25 days
        }
        private Object readResolve() {
            return YEARS;
        }
    }

    //-----------------------------------------------------------------------
    /** Rule class. */
    private static final class Rule extends DateTimeRule implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * Ordinal for performance and serialization.
         */
        private final int ordinal;

        /**
         * Restricted constructor.
         */
        private Rule(int ordinal, 
                String name,
                PeriodUnit periodUnit,
                PeriodUnit periodRange,
                int minimumValue,
                int smallestMaximumValue,
                int maximumValue) {
            super(CopticChronology.INSTANCE, name, periodUnit, periodRange, DateTimeRuleRange.of(minimumValue, smallestMaximumValue, maximumValue));
            this.ordinal = ordinal;  // 16 multiplier allow space for new rules
        }

        /**
         * Deserialize singletons.
         * 
         * @return the resolved value, not null
         */
        private Object readResolve() {
            return RULE_CACHE[ordinal / 16];
        }

        //-----------------------------------------------------------------------
        @Override
        protected DateTimeField derive(Calendrical calendrical) {
            CopticDate date = calendrical.get(CopticDate.rule());
            if (date != null) {
                switch (ordinal) {
                    case DAY_OF_WEEK_ORDINAL: return field(date.getDayOfWeek().getValue());
                    case DAY_OF_MONTH_ORDINAL: return field(date.getDayOfMonth());
                    case DAY_OF_YEAR_ORDINAL: return field(date.getDayOfYear());
                    case MONTH_OF_YEAR_ORDINAL: return field(date.getMonthOfYear());
                    case YEAR_ORDINAL: return field(date.getYear());
                }
            }
            return null;
        }
        @Override
        protected void merge(CalendricalMerger merger) {
            CopticChronology.merge(merger);
        }
        @Override
        public DateTimeRuleRange getRange(Calendrical calendrical) {
            switch (ordinal) {
                case DAY_OF_MONTH_ORDINAL: {
                    DateTimeField moy = calendrical.get(CopticChronology.MONTH_OF_YEAR);
                    if (moy != null) {
                        if (moy.getValue() == 13) {
                            DateTimeField year = calendrical.get(CopticChronology.YEAR);
                            if (year != null) {
                                return DateTimeRuleRange.of(1, isLeapYear(year.getValue()) ? 6 : 5);
                            }
                            return DateTimeRuleRange.of(1, 5, 6);
                        } else {
                            return DateTimeRuleRange.of(1, 30);
                        }
                    }
                    break;
                }
                case DAY_OF_YEAR_ORDINAL: {
                    DateTimeField year = calendrical.get(CopticChronology.YEAR);
                    if (year != null) {
                        return DateTimeRuleRange.of(1, isLeapYear(year.getValidIntValue()) ? 366 : 365);
                    }
                    break;
                }
            }
            return super.getRange();
        }

        //-----------------------------------------------------------------------
        @Override
        public int compareTo(CalendricalRule<?> other) {
            if (other instanceof Rule) {
                return ordinal - ((Rule) other).ordinal;
            }
            return super.compareTo(other);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Rule) {
                return ordinal == ((Rule) obj).ordinal;
            }
            return super.equals(obj);
        }

        @Override
        public int hashCode() {
            return Rule.class.hashCode() + ordinal;
        }
    }

    //-----------------------------------------------------------------------
    private static final int DAY_OF_WEEK_ORDINAL =          0 * 16;
    private static final int DAY_OF_MONTH_ORDINAL =         1 * 16;
    private static final int DAY_OF_YEAR_ORDINAL =          2 * 16;
    private static final int MONTH_OF_YEAR_ORDINAL =        3 * 16;
    private static final int YEAR_ORDINAL =                 4 * 16;

    //-----------------------------------------------------------------------
    /**
     * The rule for the Coptic day-of-week field.
     * <p>
     * This field uses the ISO-8601 values for the day-of-week.
     * These define Monday as value 1 to Sunday as value 7.
     * <p>
     * The enum {@link DayOfWeek} should be used wherever possible in
     * applications when referring to the day of the week value to avoid
     * needing to remember the values from 1 to 7.
     */
    public static final DateTimeRule DAY_OF_WEEK = new Rule(DAY_OF_WEEK_ORDINAL, "DayOfWeek", DAYS, WEEKS, 1, 7, 7);
    /**
     * The rule for the Coptic day-of-month field in the ISO chronology.
     * <p>
     * This field counts days sequentially from the start of the month.
     * The values are from 1 to 30 in most months, and 1 to 5 or 6 in month 13.
     */
    public static final DateTimeRule DAY_OF_MONTH = new Rule(DAY_OF_MONTH_ORDINAL, "DayOfMonth", DAYS, MONTHS, 1, 5, 30);
    /**
     * The rule for the Coptic day-of-year field in the ISO chronology.
     * <p>
     * This field counts days sequentially from the start of the year.
     * The first day of the year is 1 and the last is 365, or 366 in a leap year.
     */
    public static final DateTimeRule DAY_OF_YEAR = new Rule(DAY_OF_YEAR_ORDINAL, "DayOfYear", DAYS, YEARS, 1, 365, 366);
    /**
     * The rule for the Coptic month-of-year field in the ISO chronology.
     * <p>
     * This field counts months sequentially from the start of the year.
     * The values are from 1 to 13.
     */
    public static final DateTimeRule MONTH_OF_YEAR = new Rule(MONTH_OF_YEAR_ORDINAL, "MonthOfYear", MONTHS, YEARS, 1, 13, 13);
    /**
     * The rule for the Coptic year field in the ISO chronology.
     * <p>
     * This field counts years from the Coptic calendar epoch.
     */
    public static final DateTimeRule YEAR = new Rule(YEAR_ORDINAL, "Year", YEARS, null, CopticDate.MIN_YEAR, CopticDate.MAX_YEAR, CopticDate.MAX_YEAR);

    /**
     * Cache of rules for deserialization.
     * Indices must match ordinal passed to rule constructor.
     */
    private static final DateTimeRule[] RULE_CACHE = new DateTimeRule[] {
        DAY_OF_WEEK, DAY_OF_MONTH, DAY_OF_YEAR, MONTH_OF_YEAR, YEAR,
    };

}
