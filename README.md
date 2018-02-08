NullCore
==========
Large core system for MC servers
--------------------------
NullCore is a aio system which can uphold an entire server and make an environment which allows for many different features.
Officially made for and by EXPVP.net open source for users to see usage and have fun with it on their own.

Author
--------
The author of this plugin is ExoticCode or NullBytes
<br>
Found on <a href="https://www.spigotmc.org/members/nullbytes.142730/">Spigot</a>
<br>
Email: null.user14@gmail.com

Developers
----------
You can the implements container which holds all the plugin's instances and modify based on that.
```java
NullContainer container = NullContainer.getInstance();
```
With this container you can navigate everything in plugin for the most part. It gives you freedom to modify most things in the container to implement your own features.

Alternate to this, you can create your own container and create your own port off this plugin (not recommended)
This is not recommended because in the current Container you can already change most parts of it and really make it your own.

<b>Creating Modules:</b>
Modules in code are things like mini plugins which act like plugins and can hold data without being a plugin.
<br>
To create a module inside a plugin you have a few options, either make the plugin class implement IModule or make an entire new class implementing
<br>
With this you can use the container to register your module which will allow it to be reloaded, and viewed in the help menu of the core
<br>
To see the implementation => see net.expvp.api.interfaces.IModule and net.expvp.core.plugin.modules.Module
<br>
<br>
I have more to do in this section as the complete explanation for developers is not currently good or present.