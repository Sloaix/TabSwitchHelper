## Demo Preview
![](https://raw.githubusercontent.com/BangBangArmy/TabSwitchHelper/master/demo.gif?raw=true)

## Usage
#### Step 1. Add the JitPack repository to your build file
```grovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### Step 2. Add the dependency
```grovy
dependencies {
    compile 'com.github.BangBangArmy:TabSwitchHelper:0.0.4'
}

```

#### Step 3. like shown below
```java
TabSwitchHelper tabSwitchHelper = new TabSwitchHelper();

//init method is deprecated

tabSwitchHelper.add(this, R.id.rb_1, R.id.rb_2, R.id.rb_3, R.id.rb_4);

tabSwitchHelper.checked(R.id.rb_1);

tabSwitchHelper.setListener(new TabSwitchHelper.OnTabStateChangedListener() {
    @Override
    public void afterTabStateChanged(CompoundButton button, boolean isChecked) {
        Toast.makeText(MainActivity.this, button.getText(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean beforeTabStateChanged(CompoundButton button, boolean isChecked) {
        if (button.getId() == R.id.rb_4) {
            Toast.makeText(MainActivity.this, "event was intercepted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
});
```

## License
MIT