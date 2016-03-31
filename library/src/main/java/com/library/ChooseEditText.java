package com.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhz on 2016/3/30.
 */
public class ChooseEditText extends FrameLayout implements View.OnClickListener, DeleteButton.OnDeleteButtonClickListener, TextWatcher {

	private List<DeleteButton> mListBtn;

	private List<String> mListStr;

	private EditText mEditText;

	private RelativeLayout mRLShowChoose;

	private LinearLayout mLLShowChoose;

	private Button btnShowEdit;

	private boolean mIsShowEdit = true;

	private OnChooseEditTextListener mOnChooseEditTextListener;

	private String inputText;

	private int mChooseBgColor;
	private int mChooseTextColor;
	private int mTextSize;
	private CharSequence mHint;

	public ChooseEditText(Context context) {
		super(context);

		inti(context);
	}

	public ChooseEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initStyle(context, attrs, 0, 0);
		inti(context);
	}

	public ChooseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initStyle(context, attrs, defStyleAttr, 0);
		inti(context);

	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public ChooseEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initStyle(context, attrs, defStyleAttr, defStyleRes);
		inti(context);
	}

	protected void inti(Context context) {
		initShowChoose(context);
		mEditText = new EditText(context);
		mEditText.setBackgroundColor(Color.TRANSPARENT);
		mEditText.addTextChangedListener(this);
		mEditText.setSingleLine();
		if (mTextSize > 0) {
			mEditText.setTextSize(mTextSize);
		}
		if (mHint != null) {
			setHint(mHint.toString());
		}
		setBackgroundResource(R.drawable.bg_default);
		addView(mEditText);
	}

	protected void initStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		mChooseTextColor=Color.WHITE;
		mChooseBgColor=Color.GRAY;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ChooseEditText, defStyleAttr, defStyleRes);
		for (int i = 0, count = a.getIndexCount(); i < count; i++) {
			int attr = a.getIndex(i);
			if (attr == R.styleable.ChooseEditText_cet_TextSize) {
				mTextSize = a.getDimensionPixelSize(attr, 0);
			} else if (attr == R.styleable.ChooseEditText_cet_Hint) {
				mHint = a.getText(attr);
			} else if (attr == R.styleable.ChooseEditText_cet_ChooseBgColor) {
				mChooseBgColor = a.getColor(attr, 0);
			} else if (attr == R.styleable.ChooseEditText_cet_ChooseTextColor) {
				mChooseTextColor = a.getColor(attr, 0);
			}
		}
	}

	private void initShowChoose(Context context) {
		mRLShowChoose = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_show_choose, null);
		mLLShowChoose = (LinearLayout) mRLShowChoose.findViewById(R.id.show_choose_ll);
		btnShowEdit = (Button) mRLShowChoose.findViewById(R.id.button);
		btnShowEdit.setOnClickListener(this);
		addView(mRLShowChoose);
	}

	public void setOnChooseEditTextListener(OnChooseEditTextListener listener) {
		mOnChooseEditTextListener = listener;
	}

	public String getText() {
		return inputText;
	}

	/**
	 * Sets the text to be displayed when the text of the TextView is empty,
	 *
	 */
	public void setHint(String hint) {
		mEditText.setHint(hint);
	}

	/**
	 * Sets the text to be displayed when the text of the TextView is empty,
	 * from a resource.
	 *
	 */
	public  void setHint(int resid) {
		CharSequence hint = getContext().getResources().getText(resid);
		mEditText.setHint(hint);
	}


	private String getMathchingText() {
		if (mListStr == null || mListStr.size() == 0) return "";

		StringBuilder sb = new StringBuilder();
		for (String str : mListStr) {
			sb.append(str);
		}
		return sb.toString();
	}

	public void addItem(String text) {
		if (mIsShowEdit) {//正显示输入框中，把原有数据组成一个delBtn
			addItemFirst(text);
		} else {
			DeleteButton deleteButton = genDeleBtn(text);
			mLLShowChoose.addView(deleteButton);
			addBtnToBtnList(deleteButton);
			addStr(text);
			if (inputText == null) {
				inputText = text;
			} else {
				inputText = inputText + text;
			}
		}
		inputChange();
	}

	private void addItemFirst(String text) {
		removeAllItem();
		if (!isEmpty(inputText)) {
			DeleteButton deleteButton = genDeleBtn(inputText);
			mLLShowChoose.addView(deleteButton);
			if (null == mListBtn) mListBtn = new ArrayList<>();
			mListBtn.add(deleteButton);
			if (null == mListStr) mListStr = new ArrayList<>();
			mListStr.add(inputText);
		}
		showChooseView();
		addItem(text);
	}


	public void showEdit() {
		if (mIsShowEdit) return;
		mRLShowChoose.setVisibility(INVISIBLE);
		mEditText.setVisibility(VISIBLE);
		mIsShowEdit = true;
		if (inputText != null){
			mEditText.setText(inputText);
			mEditText.setSelection(inputText.length());
		}
	}

	public void showChooseView() {
		if (mIsShowEdit) {
			mRLShowChoose.setVisibility(VISIBLE);
			mEditText.setVisibility(INVISIBLE);
			mIsShowEdit = false;
		}
	}


	private void removeItem(long index) {
		int size = mListBtn.size();
		for (int i = 0; i < size; i++) {
			DeleteButton deleteButton = mListBtn.get(i);
			if (deleteButton.getIndex() == index) {
				mLLShowChoose.removeView(deleteButton);
				mListBtn.remove(deleteButton);
				mListStr.remove(i);
				inputText = getMathchingText();
				inputChange();
				return;
			}
		}


	}

	public void removeAllItem() {
		if (mListStr != null) {
			mListStr.clear();
		}
		if (mListBtn != null) {
			for (DeleteButton deleteButton : mListBtn) {
				mLLShowChoose.removeView(deleteButton);
			}
			mListBtn.clear();
		}

	}

	/**
	 * add a string to mListStr
	 *
	 * @param str the string you want to add
	 */
	private void addStr(String str) {
		if (null == mListStr) mListStr = new ArrayList<>();
		mListStr.add(str);
	}



	private void addBtnToBtnList(DeleteButton btn) {
		if (null == mListBtn) mListBtn = new ArrayList<>();
		mListBtn.add(btn);
	}

	@Override
	public void onClick(View v) {
		if (R.id.button == v.getId()) {
			removeAllItem();
			showEdit();
		}

	}


	@Override
	public void onDelecteClick( long index) {
		removeItem(index);
	}

	@Override
	public void onContentClick(String text) {
		showEdit();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		inputText = s.toString();
		inputChange();
	}

	private void inputChange() {
		if (mOnChooseEditTextListener != null) {
			mOnChooseEditTextListener.onTextChangeed(inputText);
		}
	}

	/**
	 * return if str is empty
	 *
	 * @param str :the string
	 * @return   : if str is empty
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals(""));
	}

	private DeleteButton genDeleBtn(String text) {
		DeleteButton deleteButton = new DeleteButton(getContext());
		deleteButton.setTextColor(mChooseTextColor);
		deleteButton.setBgColor(mChooseBgColor);
		if (mTextSize > 0) {
			deleteButton.setTextSize(mTextSize);
		}
		deleteButton.setText(text);
		deleteButton.setOnDeleteButtonClickListener(this);
		return deleteButton;
	}
}
