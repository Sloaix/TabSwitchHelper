## Demo Preview
![](https://raw.githubusercontent.com/lsxiao/TabSwichHelper/master/demo.gif?raw=true)

## Useage
```java
TabSwitchHelper tabSwitchHelper = new TabSwitchHelper();
tabSwitchHelper.init(this, R.id.rb_1, R.id.rb_2, R.id.rb_3, R.id.rb_4);
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