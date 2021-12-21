# LWM
A lightweight inventory menu library for the Spigot API.

## Usage
### LightweightMenuHandler
1. To start using LWM, you'll need to create an instance of LightweightMenuHandler. You should do this in your plugins main class.

```java
public class MyPlugin extends JavaPlugin {

    private LightweightMenuHandler lwmHandler;
  
    @Override
    public void onEnable() {
        this.lwmHandler = new LightweightMenuHandler();
    }
  
}
```

</br>

2. After you have an instance of LightweightMenuHandler, you need to register it as an event listener. You can do this with the plugin manager or use the utility method 'registerListener'. 

```java
@Override
public void onEnable() {
    this.lwmHandler = new LightweightMenuHandler();
    this.lwmHandler.registerListener(this);
}
```

</br>
</br>

### LightweightMenu
1. Now you can create your first menu. You'll start by making a child class of LightweightMenu. The constructor takes a menu title as well as a row count. 

```java
public class MyMenu extends LightweightMenu {

  public MyMenu() {
      super("My lightweight menu!", 3);
  }

}
```

</br>

2. In the constructor you start customizing and adding components to your menu. Currently LWM supports toggleable dragging, buttons, slots, and static items. 

```java
public class MyMenu extends LightweightMenu {

    public MyMenu() {
        super("My lightweight menu!", 3);
      
        setAllowDragging(false);
        addMenuComponents(
            // New button at inventory slot 20. Lambda parameters are the player who clicked the button and the menu that was clicked
            new ClickComponent(this, 20, (player, menu) -> {
                // Do something!!
            }),
            
            // New slot at inventory slot 20. Lambda parameters are the player who modified the slot, the inventory modified and the new stack in that slot
            new SlotComponent(this, 21, (player, inventory, newStack) -> {
                // Do something!
            }),
            
            // New static item at inventory slot 22
            new ItemComponent(this, 22, Material.BLACK_STAINED_GLASS_PANE)
        );
    }
}
```
