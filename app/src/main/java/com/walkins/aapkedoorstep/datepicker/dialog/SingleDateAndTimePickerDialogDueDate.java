package com.walkins.aapkedoorstep.datepicker.dialog;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.walkins.aapkedoorstep.R;
import com.walkins.aapkedoorstep.datepicker.DateHelper;
import com.walkins.aapkedoorstep.datepicker.SingleDateAndTimePicker;
import com.walkins.aapkedoorstep.datepicker.widget.DateWithLabel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.walkins.aapkedoorstep.datepicker.widget.SingleDateAndTimeConstants.STEP_MINUTES_DEFAULT;


public class SingleDateAndTimePickerDialogDueDate extends BaseDialog {

    private final DateHelper dateHelper = new DateHelper();
    private Listener listener;
    private BottomSheetHelper bottomSheetHelper;
    private SingleDateAndTimePicker picker;


    @Nullable
    private String title;
    @Nullable
    private Integer titleTextSize;
    @Nullable
    private Integer bottomSheetHeight;
    @Nullable
    private String todayText;
    @Nullable
    private DisplayListener displayListener;

    public SingleDateAndTimePickerDialogDueDate(Boolean isResetOff, Context context) {
        this(context, false);
    }

    private SingleDateAndTimePickerDialogDueDate(Context context, boolean bottomSheet) {
        final int layout = bottomSheet ? R.layout.bottom_sheet_picker_bottom_sheet :
                R.layout.bottom_sheet_picker;
        this.bottomSheetHelper = new BottomSheetHelper(context, layout);

        this.bottomSheetHelper.setListener(new BottomSheetHelper.Listener() {
            @Override
            public void onOpen() {
            }

            @Override
            public void onLoaded(View view) {
                init(view);
                if (displayListener != null) {
                    displayListener.onDisplayed(picker);
                }
            }

            @Override
            public void onClose() {
                SingleDateAndTimePickerDialogDueDate.this.onClose();

                if (displayListener != null) {
                    displayListener.onClosed(picker);
                }
            }
        });
    }


    private void init(View view) {
        picker = (SingleDateAndTimePicker) view.findViewById(R.id.picker);
        picker.setDateHelper(dateHelper);
        if (picker != null) {
            if (bottomSheetHeight != null) {
                ViewGroup.LayoutParams params = picker.getLayoutParams();
                params.height = bottomSheetHeight;
                picker.setLayoutParams(params);
            }
        }
        final ImageView ivClose = view.findViewById(R.id.ivClose);
        if (ivClose != null) {
            ivClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CloseClicked = true;
                    close();
                }
            });
        }
        final Button buttonOk = (Button) view.findViewById(R.id.btn_confirm);
        if (buttonOk != null) {
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    okClicked = true;
                    close();
                }
            });

            if (mainColor != null) {
                buttonOk.setTextColor(backgroundColor);
            }

            if (titleTextSize != null) {
                buttonOk.setTextSize(titleTextSize);
            }
        }
        final Button btn_reset = (Button) view.findViewById(R.id.btn_reset);

        btn_reset.setText("Cancel");

//        btn_reset.setVisibility(View.GONE);

        if (btn_reset != null) {
            btn_reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ResetClicked = true;
                    close();
                }
            });

            if (mainColor != null) {
                btn_reset.setTextColor(mainColor);
            }

            if (titleTextSize != null) {
                btn_reset.setTextSize(titleTextSize);
            }
        }

        final View sheetContentLayout = view.findViewById(R.id.sheetContentLayout);
        if (sheetContentLayout != null) {
            sheetContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            if (backgroundColor != null) {
                sheetContentLayout.setBackgroundColor(backgroundColor);
            }
        }

        /*final TextView titleTextView = (TextView) view.findViewById(R.id.sheetTitle);
        if (titleTextView != null) {
            titleTextView.setText(title);

            if (titleTextColor != null) {
                titleTextView.setTextColor(titleTextColor);
            }

            if (titleTextSize != null) {
                titleTextView.setTextSize(titleTextSize);
            }
        }*/

        picker.setTodayText(
                new DateWithLabel(todayText, new Date()));

        final View pickerTitleHeader = view.findViewById(R.id.pickerTitleHeader);
        if (mainColor != null && pickerTitleHeader != null) {
            pickerTitleHeader.setBackgroundColor(mainColor);
        }

        if (curved) {
            picker.setCurved(true);
            picker.setVisibleItemCount(7);
        } else {
            picker.setCurved(false);
            picker.setVisibleItemCount(5);
        }
        picker.setMustBeOnFuture(mustBeOnFuture);

        picker.setStepSizeMinutes(minutesStep);

        if (dayFormatter != null) {
            picker.setDayFormatter(dayFormatter);
        }

        if (customLocale != null) {
            picker.setCustomLocale(customLocale);
        }

        if (mainColor != null) {
            picker.setSelectedTextColor(mainColor);
        }

        // displayYears used in setMinDate / setMaxDate
        picker.setDisplayYears(displayYears);

        if (minDate != null) {
            picker.setMinDate(minDate);
        }

        if (maxDate != null) {
            picker.setMaxDate(maxDate);
        }

        if (defaultDate != null) {
            picker.setDefaultDate(defaultDate);
        }

        if (isAmPm != null) {
            picker.setIsAmPm(isAmPm);
        }


        picker.setDisplayDays(displayDays);
        picker.setDisplayMonths(displayMonth);
        picker.setDisplayDaysOfMonth(displayDaysOfMonth);
        picker.setDisplayMinutes(displayMinutes);
        picker.setDisplayHours(displayHours);
    }

    public SingleDateAndTimePickerDialogDueDate setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setCurved(boolean curved) {
        this.curved = curved;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setMinutesStep(int minutesStep) {
        this.minutesStep = minutesStep;
        return this;
    }

    private void setDisplayListener(DisplayListener displayListener) {
        this.displayListener = displayListener;
    }

    public SingleDateAndTimePickerDialogDueDate setTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setTitleTextSize(@Nullable Integer titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setBottomSheetHeight(@Nullable Integer bottomSheetHeight) {
        this.bottomSheetHeight = bottomSheetHeight;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setTodayText(@Nullable String todayText) {
        this.todayText = todayText;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setMustBeOnFuture(boolean mustBeOnFuture) {
        this.mustBeOnFuture = mustBeOnFuture;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setMinDateRange(Date minDate) {
        this.minDate = minDate;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setMaxDateRange(Date maxDate) {
        this.maxDate = maxDate;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDisplayDays(boolean displayDays) {
        this.displayDays = displayDays;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDisplayMinutes(boolean displayMinutes) {
        this.displayMinutes = displayMinutes;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDisplayMonthNumbers(boolean displayMonthNumbers) {
        this.displayMonthNumbers = displayMonthNumbers;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDisplayHours(boolean displayHours) {
        this.displayHours = displayHours;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDisplayDaysOfMonth(boolean displayDaysOfMonth) {
        this.displayDaysOfMonth = displayDaysOfMonth;
        return this;
    }


    private SingleDateAndTimePickerDialogDueDate setDisplayMonth(boolean displayMonth) {
        this.displayMonth = displayMonth;
        return this;
    }

    private SingleDateAndTimePickerDialogDueDate setDisplayYears(boolean displayYears) {
        this.displayYears = displayYears;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setDayFormatter(SimpleDateFormat dayFormatter) {
        this.dayFormatter = dayFormatter;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setCustomLocale(Locale locale) {
        this.customLocale = locale;
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setIsAmPm(boolean isAmPm) {
        this.isAmPm = Boolean.valueOf(isAmPm);
        return this;
    }

    public SingleDateAndTimePickerDialogDueDate setFocusable(boolean focusable) {
        bottomSheetHelper.setFocusable(focusable);
        return this;
    }

    private SingleDateAndTimePickerDialogDueDate setTimeZone(TimeZone timeZone) {
        dateHelper.setTimeZone(timeZone);
        return this;
    }

    @Override
    public void display() {
        super.display();
        bottomSheetHelper.display();
    }

    @Override
    public void close() {
        super.close();
        bottomSheetHelper.hide();

        if (listener != null && okClicked) {
            Log.e("getdatee11", "" + picker.getDate());
            listener.onDateSelected(picker.getDate(), "");
        }
        if (listener != null && ResetClicked) {
            Log.e("getdatee11", "" + picker.getDate());
            listener.onDateSelected(picker.getDate(), "Cancel");
        }
        if (listener != null && CloseClicked) {
            Log.e("getdatee11", "" + picker.getDate());
            listener.onDateSelected(picker.getDate(), "Close");
        }

    }

    @Override
    public void dismiss() {
        super.dismiss();
        bottomSheetHelper.dismiss();
    }

    public interface Listener {
        void onDateSelected(Date date, String s);
    }

    public interface DisplayListener {
        void onDisplayed(SingleDateAndTimePicker picker);

        void onClosed(SingleDateAndTimePicker picker);
    }

    public static class Builder {
        private final Context context;
        private SingleDateAndTimePickerDialogDueDate dialog;

        @Nullable
        private Listener listener;
        @Nullable
        private DisplayListener displayListener;

        @Nullable
        private String title;

        @Nullable
        private Integer titleTextSize;

        @Nullable
        private Integer bottomSheetHeight;

        @Nullable
        private String todayText;

        private boolean bottomSheet;

        private boolean curved;
        private boolean mustBeOnFuture;
        private int minutesStep = STEP_MINUTES_DEFAULT;

        private boolean displayDays = true;
        private boolean displayMinutes = true;
        private boolean displayHours = true;
        private Boolean isResetOff = false;
        private boolean displayMonth = false;
        private boolean displayDaysOfMonth = false;
        private boolean displayYears = false;
        private boolean displayMonthNumbers = false;
        private boolean focusable = false;

        @Nullable
        private Boolean isAmPm;

        @ColorInt
        @Nullable
        private Integer backgroundColor = null;

        @ColorInt
        @Nullable
        private Integer mainColor = null;

        @ColorInt
        @Nullable
        private Integer titleTextColor = null;

        @Nullable
        private Date minDate;
        @Nullable
        private Date maxDate;
        @Nullable
        private Date defaultDate;

        @Nullable
        private SimpleDateFormat dayFormatter;

        @Nullable
        private Locale customLocale;
        private TimeZone timeZone;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(@Nullable String title) {
            this.title = title;
            return this;
        }

        public Builder titleTextSize(@Nullable Integer titleTextSize) {
            this.titleTextSize = titleTextSize;
            return this;
        }

        public Builder bottomSheetHeight(@Nullable Integer bottomSheetHeight) {
            this.bottomSheetHeight = bottomSheetHeight;
            return this;
        }

        public Builder todayText(@Nullable String todayText) {
            this.todayText = todayText;
            return this;
        }

        public Builder bottomSheet() {
            this.bottomSheet = true;
            return this;
        }

        public Builder curved() {
            this.curved = true;
            return this;
        }

        public Builder mustBeOnFuture() {
            this.mustBeOnFuture = true;
            return this;
        }

        public Builder minutesStep(int minutesStep) {
            this.minutesStep = minutesStep;
            return this;
        }

        public Builder displayDays(boolean displayDays) {
            this.displayDays = displayDays;
            return this;
        }

        public Builder displayAmPm(boolean isAmPm) {
            this.isAmPm = isAmPm;
            return this;
        }

        public Builder displayMinutes(boolean displayMinutes) {
            this.displayMinutes = displayMinutes;
            return this;
        }

        public Builder displayHours(boolean displayHours) {
            this.displayHours = displayHours;
            return this;
        }

        public Builder isReserOff(boolean isResetOff) {
            this.isResetOff = isResetOff;
            return this;
        }

        public Builder displayDaysOfMonth(boolean displayDaysOfMonth) {
            this.displayDaysOfMonth = displayDaysOfMonth;
            return this;
        }

        public Builder displayMonth(boolean displayMonth) {
            this.displayMonth = displayMonth;
            return this;
        }

        public Builder displayYears(boolean displayYears) {
            this.displayYears = displayYears;
            return this;
        }

        public Builder listener(@Nullable Listener listener) {
            this.listener = listener;
            return this;
        }

        public Builder displayListener(@Nullable DisplayListener displayListener) {
            this.displayListener = displayListener;
            return this;
        }

        public Builder titleTextColor(@NonNull @ColorInt int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder backgroundColor(@NonNull @ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder mainColor(@NonNull @ColorInt int mainColor) {
            this.mainColor = mainColor;
            return this;
        }

        public Builder minDateRange(Date minDate) {
            this.minDate = minDate;
            return this;
        }

        public Builder maxDateRange(Date maxDate) {
            this.maxDate = maxDate;
            return this;
        }

        public Builder displayMonthNumbers(boolean displayMonthNumbers) {
            this.displayMonthNumbers = displayMonthNumbers;
            return this;
        }

        public Builder defaultDate(Date defaultDate) {
            this.defaultDate = defaultDate;
            return this;
        }

        public Builder setDayFormatter(SimpleDateFormat dayFormatter) {
            this.dayFormatter = dayFormatter;
            return this;
        }

        public Builder customLocale(Locale locale) {
            this.customLocale = locale;
            return this;
        }

        public Builder setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        public Builder focusable() {
            this.focusable = true;
            return this;
        }

        public SingleDateAndTimePickerDialogDueDate build() {
            final SingleDateAndTimePickerDialogDueDate dialog = new SingleDateAndTimePickerDialogDueDate(context, bottomSheet)
                    .setTitle(title)
                    .setTitleTextSize(titleTextSize)
                    .setBottomSheetHeight(bottomSheetHeight)
                    .setTodayText(todayText)
                    .setListener(listener)
                    .setCurved(curved)
                    .setMinutesStep(minutesStep)
                    .setMaxDateRange(maxDate)
                    .setMinDateRange(minDate)
                    .setDefaultDate(defaultDate)
                    .setDisplayHours(displayHours)
                    .setDisplayMonth(displayMonth)
                    .setDisplayYears(displayYears)
                    .setDisplayDaysOfMonth(displayDaysOfMonth)
                    .setDisplayMinutes(displayMinutes)
                    .setDisplayMonthNumbers(displayMonthNumbers)
                    .setDisplayDays(displayDays)
                    .setDayFormatter(dayFormatter)
                    .setCustomLocale(customLocale)
                    .setMustBeOnFuture(mustBeOnFuture)
                    .setTimeZone(timeZone)
                    .setFocusable(focusable);

            if (mainColor != null) {
                dialog.setMainColor(mainColor);
            }

            if (backgroundColor != null) {
                dialog.setBackgroundColor(backgroundColor);
            }

            if (titleTextColor != null) {
                dialog.setTitleTextColor(titleTextColor);
            }

            if (displayListener != null) {
                dialog.setDisplayListener(displayListener);
            }

            if (isAmPm != null) {
                dialog.setIsAmPm(isAmPm);
            }

            return dialog;
        }

        public void display() {
            dialog = build();
            dialog.display();
        }

        public void close() {
            if (dialog != null) {
                dialog.close();
            }
        }

        public void dismiss() {
            if (dialog != null)
                dialog.dismiss();
        }
    }

}
