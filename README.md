# Supaplex level editor
![demo](https://raw.githubusercontent.com/mokyu/supaplex-level-editor/assets/assets/demo.jpg)
### An open and multi platform level editor for Supaplex
I've always loved this little Boulder Dash clone from 1991. Ofcourse level editors for this game already [exist](https://www.elmerproductions.com/sp/software.html), but this does not include modern implementations with readily available source code. The goal of this project is to develop a modern multi platform free and open source Supaplex level editor, and of course as a tribute to the original game.

### features

 - Ability to load,save and export level collections (`LEVELS.DAT`) and individual levels (`*.SP`)
 - Pencil, line, rect and fill tool to help you create your levels.
 - Multi language support. English, Dutch and French (Thanks to Raphaël G.)

### Code Example
The editor is made of 2 packages. `mokyu.libsupaplex` and `mokyu.libsupaplexLevelEditor`. The latter is an easy to use API for interacting with existing and creating new Supaplex levels from scratch. While the former is the level editor itself.

   ```java
// create an empty level collection with 111 empty levels
Supaplex supaplex = new Supaplex(); 

// load an existing level collection
try {
	supaplex.loadLevelPackFromFile("C:\dosbox\supaplex\MYLEVELS.DAT")
} catch (IOException e) {
	// handle IO error
} catch (Exception e) {
	// handle incorrect LEVELS.DAT file
}

// save a level collection to file
try {
	supaplex.saveLevelPackToFile("C:\dosbox\supaplex\MYLEVELS.DAT")
} catch (IOException e) {
	// handle IO error
}

// grab an idividual level

Level level = supaplex.getLevel(0);

// get level name
System.out.Println(level.getName()); // "------DONT PANIC-------"

// get a specific Tile from the level
System.out.Println(level.getTile(new Point(17,12)).getNiceName()); // "Hardware (Standard)" 
```
For more info on libsupaplex you can clone the project and generate a javadoc.

### Installation
• Install JDK8

• Download the latest level editor release from the releases section.

• `unzip` and open `editor.jar`

### License
GPLv3 © (Mokyu)

2019
