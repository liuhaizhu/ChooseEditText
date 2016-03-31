# ChooseEditText
ChooseEditText is an edit that can add tag in edit space

![](https://github.com/liuhaizhu/ChooseEditText/blob/master/art/example.gif)
[Examples in youtube](https://youtu.be/pL4dWoM6h_Q)
How to use
=====
Gradle Dependency
```gradle
compile 'com.chooseedittext:library:1.0.0'
```

Usage
=====
Declare a weekselection inside your layout XML file like this:
```xml
    <com.library.ChooseEditText
        android:id="@+id/chooseedittext"
        app:cet_Hint="Input content "
        app:cet_TextSize="10sp"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:layout_margin="4dp">
    </com.library.ChooseEditText>
```
add tag
```java
String text="Nike";
chooseEditText.addItem(text);
```
To monitor text changes
```java
chooseEditText.setOnChooseEditTextListener(new OnChooseEditTextListener() {
			@Override
			public void onTextChangeed(String text) {
			// do something
				tvResult.setText(text);
			}
		});
```
