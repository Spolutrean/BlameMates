# BlameMates

### Motivation & introduction
Development in a team requires constant communication, especially when it comes to the changes made in code. `git blame` helps with finding out who edited a particular line last, but manually opening your messenger of choice and finding that person is a bothersome process, even more so if the team is large, its members all use different messengers and/or you are new to the team and have not yet adjusted to how the things work. *We also needed a plugin development idea for a university project, and this one seemed fun :)*

### Description & usage
**BlameMates** is the solution to this problem. By choosing a corresponding right-click menu item (or using a designated keyboard shortcut) you can contact the last editor of that line via any of the communication methods they have provided without having to open anything manually. The same menu houses a way for you to change the communication methods for both yourself and other team members, should this be necessary. All of this data is stored in a file in the project's root folder, and can be easily accessed and edited manually in case there is a need for that. The plugin data file is kept up to date by your VCS of choice.
