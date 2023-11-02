# 一个支持#话题、@用户的 EditText

**_应用场景：类似于微博发布页#话题、@用户_**

## 使用

[![](https://jitpack.io/v/lrCzh/Xspan.svg)](https://jitpack.io/#lrCzh/Xspan)

### 1、引入

```
allprojects {
	repositories {
	    ...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
    implementation 'com.github.lrCzh:Xspan:latestVersion'
}
```

### 2、XML布局

```
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <com.xh.xspan.XSpanEditText
        android:id="@+id/et"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:gravity="start|top"
        android:hint="请输入"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
        
</androidx.constraintlayout.widget.ConstraintLayout>
```

### 3、创建相关的`TextSpan`和`DataClass`

话题信息

```
@Parcelize
data class Topic(val id: Int, val name: String) : Parcelable
```

用户信息

```
@Parcelize
data class User(val id: Int, val name: String) : Parcelable
```

TopicSpan（#话题），继承自TextSpan

```
@Parcelize
class TopicSpan(val topic: Topic) : TextSpan() {

    /**
     * 要展示的文本内容
     */
    override val displayText = "#${topic.name} "

    /**
     * 要展示的样式
     * 注！！！，实现时只能以直接赋值的方式，不能以自定义属性访问器的方式
     */
    override val displaySpan = ForegroundColorSpan(Color.RED)
}
```

AtSpan（@用户），继承自TextSpan，这里实现了IntegratedSpan接口，表示这个Span具有一体性，删除时会整个被删除

```
@Parcelize
class AtSpan(val user: User) : TextSpan(), IntegratedSpan {

    /**
     * 要展示的文本内容
     */
    override val displayText = "@${user.name} "

    /**
     * 要展示的样式
     * 注！！！，实现时只能以直接赋值的方式，不能以自定义属性访问器的方式
     */
    override val displaySpan = ForegroundColorSpan(Color.BLUE)
}
```

### 4、插入和获取

```
// 插入话题
et.insertTextSpan(TopicSpan(Topic(1, "话题1")))

// 插入at
et.insertTextSpan(AtSpan(User(1, "用户1")))

// 获取所有的TopicSpan
et.getSpans(TopicSpan::class.java)

// 获取所有的AtSpan
et.getSpans(AtSpan::class.java)
```

### 5、特殊字符拦截

在XML里面给 XSpanEditText 追加两个属性：

specialCharSet：需要触发回调的特殊字符。  
interceptSpecialChar：是否拦截该特殊字符的输入。（为 true 时，仅触发回调，但字符不会被输入；为
false时，触发回调并且能被正常输入）

```
<com.xh.xspan.XSpanEditText
    android:id="@+id/et"
    android:layout_width="match_parent"
    android:layout_height="200dp"
    android:gravity="start|top"
    android:hint="请输入"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:specialCharSet="#@"
    app:interceptSpecialChar="false"/>
```

然后在 Activity 给 XSpanEditText 设置回调监听

```
et.setOnSpecialCharInputAction { specialChar ->
    when (specialChar) {
        '#' -> {
            // 在这里执行输入#的相关操作
            Toast.makeText(this@MainActivity, "输入了 #", Toast.LENGTH_SHORT).show()
        }

        '@' -> {
            // 在这里执行输入@的相关操作
            Toast.makeText(this@MainActivity, "输入了 @", Toast.LENGTH_SHORT).show()
        }
    }
}
```

## 完。
