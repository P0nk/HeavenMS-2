# Cosmic - MapleStory v83
![example workflow](https://github.com/owenhelloworld/Cosmic/actions/workflows/pr-pipeline.yml/badge.svg)

Cosmic launched as a successor to HeavenMS on March 21st 2021.

This document is currently being worked on, so it may not be fully accurate.

## Beware

***This emulator is not production ready.***

It can be useful for testing things locally or for trying out ideas, but launching a new private server based on this
with no real changes is not recommended.

---

### Development information

#### Status (updated 28/9/21)

Development is <span style="color:Orange">**on pause**</span>, but any submitted PRs will be reviewed.

#### Ways to contribute

* Submit a Pull Request (fork -> commit -> PR)
* Submit a bug report (add a new issue on GitHub, or post in `bug-report` on Discord)
* Spread the word about Cosmic

#### Community

GitHub: https://github.com/P0nk/Cosmic  
Discord: https://discord.gg/JU5aQapVZK

---

## Tools
* **Java 16 SDK** - Needed to compile and run Java code. Install manually or through IntelliJ depending on how you prefer to launch the server. Not required for launching with Docker.
  * Link: https://jdk.java.net/16/
	

* **IntelliJ IDEA** - Java IDE and your main tool for working with the source code. Community edition is good enough.
  * Link: https://www.jetbrains.com/idea/
	

* **MySQL Community Server 8** - Database for game data.  
  * Link: https://dev.mysql.com/downloads/mysql/
	

* **MySQL Workbench 8** - Client for interacting with the database. Other clients do exist. 
  * Link: https://dev.mysql.com/downloads/workbench/
	

* **Docker Desktop** (optional) - For launching the game locally with less hassle.  
  * Link: https://www.docker.com/products/docker-desktop
	

* **Client files and general tools**
  * Link: https://drive.google.com/drive/folders/1hgnb92MGL6xqEp9szEMBh0K9pSJcJ6IT?usp=sharing
	

## Client 

Latest localhost client: https://hostr.co/amuX5SLeeVZx

**Important note about localhost clients**: these executables are red-flagged by antivirus tools as __potentially malicious software__,
this happens due to the reverse engineering methods that were applied onto these software artifacts. 
Those depicted here have been put to use for years already and posed no harm so far, so they are soundly assumed to be safe.

The following list, in bottom-up chronological order, 
holds information regarding all changes that were applied from the starting localhost used in this development. 
Some lines have a link attached, that will lead you to a snapshot of the localhost at that version of the artifact. 
Naturally, later versions holds all previous changes along with the proposed changes.

**Change log:**

  * Fixed Monster Magnet crashing the caster when trying to pull fixed mobs, credits to Shavit. https://gofile.io/?c=BW7dVM (dead link)
  * Cleared need for administrator privileges (OS) to play the game, credits to Ubaware.
  * Set a higher cap for AP assigning with AP Reset, credits to Ubaware.
  * Fixed Monster Magnet crashing the caster when trying to pull bosses. Drawback: Dojo HPBar becomes unavailable. https://hostr.co/SvnSKrGzXhG0
  * Fixed some 'rn' problems with quest icons & removed "tab" from party leader changed message. https://hostr.co/tsYsQzzV6xT0
  * Removed block on applying attack-based strengthening gems on non-weapon equipments. https://hostr.co/m2bVtnizCtmD
  * Set a higher cap for SPEED.
  * Removed the AP assigning block for beginners below level 10. https://hostr.co/AHAHzneCti9B
  * Removed block on party for beginners level 10 or below. https://hostr.co/JZq53mMtToCz
  * Removed block on MTS entering in some maps, rendering the buyback option available.
  * Removed "AP excess" popup and limited actions on Admin/MWLB, credits to kevintjuh93.
  * Removed "You've gained a level!" popup, credits to PrinceReborn.
  * Removed caps for WATK, WDEF, MDEF, ACC, AVOID.
  * 'n' problem fixed.
  * Fraysa's https://hostr.co/gJbLZITRVHmv
  * Eric's MapleSilver starting on window-mode.

---

## Getting started
* Install the client
* Install the server

### Installing the client

1. From "MapleGlobal-v83-setup.exe", install MapleStory on your folder of preference (e.g. "C:\Nexon\MapleStory") and follow their instructions.
2. Once done, erase these files: "HShield" (folder), "ASPLnchr.exe", "MapleStory.exe" and "Patcher.exe".
3. Extract into the client folder the "localhost.exe" from the provided link.
4. Overwrite the original WZ files with the ones provided from either one of those folders on the Google Drive:
	- "commit397_wz" (last published RELEASE, referring to commit of same number).
	- "current_wz" (latest source update).

#### Editing localhost IP target

If you are not using "localhost" as the target IP on the server's config file, you will need to HEX-EDIT "localhost.exe" to fetch your IP. Track down all IP locations by searching for "Type: String" "127.0.0.1", and applying the changes wherever it fits.

To hex-edit, install the Neo Hex Editor from "free-hex-editor-neo.exe" and follow their instructions. Once done, open "localhost.exe" for editing and overwrite the IP values under the 3 addresses. Save the changes and exit the editor.

#### Testing the localhost

Open the "localhost.exe" client. 
If by any means the program did not open, and checking the server log your ping has been listened by the server 
and you are using Windows 8 or 10, it probably might be some compatibility issue.

In that case, extract "lolwut.exe" from "lolwut-v0.01.rar" and place it on the MapleStory client folder ("C:\Nexon\MapleStory"). 
Your "localhost.exe" property settings must follow these:

Note: "lolwut.exe" is currently not available in the Google Drive.

* Run in compatibility mode: Windows 7;
* Unchecked reduced color mode;
* 640 x 480 resolution;
* Unchecked disable display on high DPI settings;
* Run as an administrator;
* Opening "lolwut.exe", use Fraysa's method.

Important: should the client be refused a connection to the game server, it may be because of firewall issues. Head to the end of this file to proceed in allowing this connection through the computer's firewall. Alternatively, one can deactivate the firewall and try opening the client again.

---
### Installing the server 
1. Configure the project
2. Set up the database
3. Launch the server

If you are using Docker (quick start):
1. Configure the project
2. Launch the server

#### Configuring the project

The easiest way to set up your project is to clone the repository directly into a new IntelliJ project.

1. Install IntelliJ
2. Create a new "Project from Version Control..."
3. Enter the URL to this GitHub repository: "https://github.com/P0nk/Cosmic.git"
4. Click on "Clone". A new project will now be created with all the files from the repository.

#### Setting up the database

1. Install MySQL Server 8 and MySQL Workbench 8.  
2. Using Workbench, create a new user with username "cosmic_server" and password "snailshell". 
   This the default configuration in Cosmic.
   * (Optional) Restrict the Schema Privileges for this new user for improved security. 
	 Add a new entry with "Schemas matching pattern: cosmic" and only select "SELECT", "INSERT", "UPDATE", "DELETE" under "Object Rights"
3. Run the sql scripts in the "database/sql" directory of the project in the order indicated by their names. 
	* Make sure you are connected to the database with the "root" user to be able to run the scripts.
	* Run scripts one by one through the menu: "File" -> "Run SQL Script" -> select the script file to run -> "Run"
	* The 3rd script "3-db_shopupdate" is optional. It adds custom shop items for certain NPCs.
    * The 4th script "4-db_admin" is also optional, but recommended if you are new. It adds an admin account to simplify the setup.

Use this info when you connect to MySQL Server for the first time:
* Server Host: localhost
* Port: 3306
* Username: root
* Password: <whatever password you set during MySQL Server installation>

At the end of the execution of these sql scripts, you should have installed a database schema named "cosmic". 
REGISTER YOUR FIRST ACCOUNT to be used in-game by **manually creating** an entry in the table "accounts" in the database with a username and password.


### Launching the server

Configure the IP you want to use for your MapleStory server in "config.yaml" file, or set it as "localhost" if you want to run it only on your machine.
Alternatively, you can use the IP given by Hamachi to use on a Hamachi network, or you can use a non-Hamachi method of port-forwarding. Neither will be approached here.


To launch the server, you may either:
* Launch inside IntelliJ
* Launch a built jar file
* Launch with Docker

#### Launch inside IntelliJ
1. Open the file src/main/java/net/server/Server.java.
2. Click the green arrow to the left of the class definition "public class Server", and then "Run Cosmic". 
   * Alternatively (recommended), create a new Configuration that points to "net.server.Server".
3. The server launches in a terminal window inside IntelliJ.

#### Launch a jar file
1. Create the jar file
   * The jar file is created by the Maven assembly plugin in the package lifecycle.
   * If you already have Maven installed, simply run the command "mvn clean install" to create the jar file.
   * IntelliJ also comes with built-in Maven support. Open a new terminal window inside IntelliJ, type "mvn clean install" (your command should now be marked green), then Ctrl+Enter to build the jar file.
2. Launch the jar file
   * Double click on "launch.bat" (need to have Java 16 installed)
    
#### Launch with Docker
1. Start Docker
2. Run the command "docker compose up" at the root of the project.
    * If you make any changes to the code, make sure you append the "--build" option at the end of the command to force rebuild the server image.

---
### Creating an account and logging into the game

If you ran the admin sql script, there already exists an account in your database with an admin character on it. You don't need to change its GM level. Log in using these credentials:
* Username: "admin"
* Password: "admin"
* Pin: "0000"
* Pic: "000000"

By default, the server source is set to allow AUTO-REGISTERING. This means that, by simply typing in a "Login ID" and a "Password", you're able to create a new account.

After creating a character, experiment typing in all-chat "@commands". 
This will display all available commands for the current GM level your character has.

To change a character's GM level, make sure that character is not logged in, then:

1. Open MySQL Workbench;
2. Expand "cosmic" schema;
3. Expand "Tables";
4. Right-click "characters" and click "Select Rows"
5. Find your character in Result Grid. Scroll to the right and find the "gm" column.
6. Edit your character's gm value and click "Apply", and then "Apply" again in the window that appeared, then "Finish".
	* 0 is what ordinary players start with, and 6 is the highest gm value. Higher level gms have access to more commands in game.

---
### Some notes about WZ/WZ.XML EDITING 

NOTE: Be extremely wary when using server-side's XMLs data being reimported into the client's WZ, as some means of synchronization between the server and client modules, this action COULD generate some kind of bugs afterwards. Client-to-server data reimporting seems to be fine, though.

#### Editing the v83 WZ's:

* Use the HaRepacker 4.2.4 editor, encryption "GMS (old)".
* Open the desired WZ for editing and use the node hierarchy to make the desired changes (copy/pasting nodes may be unreliable in rare scenarios).
* Save the changed WZ, **overwriting the original content** at the client folder.
* Finally, **RE-EXPORT (using the "Private Server..." exporting option) the changed XMLs into the server's WZ.XML files**, overwriting the old contents.

**These steps are IMPORTANT, to maintain synchronization** between the server and client modules.

---
### Portforwarding the SERVER

To use portforward, you will need to have permission to change things on the LAN router. Access your router using the Internet browser. URLs vary accordingly with the manufacturer. To discover it, open the command prompt and type "ipconfig" and search for the "default gateway" field. The IP shown there is the URL needed to access the router. Also, look for the IP given to your machine (aka "IPv4 address" field), which will be the server one. 

The default login/password also varies, so use the link http://www.routerpasswords.com/ as reference. Usually, login as "admin" and password as "password" completes the task well.

Now you have logged in the router system, search for anything related to portforwarding. Should the system prompt you between portforwarding and portriggering, pick the first, it is what we will be using.

Now, it is needed to enable the right ports for the Internet. For Cosmic, it is basically needed to open ports 7575 to 7575 + (number of channels) and port 8484. Create a new custom service which enables that range of ports for the server's channel and opt to use TCP/UDP protocols. Finally, create a custom service now for using port 8484.

Optionally, if you want to host a webpage, portforward the port 80 (the HTTP port) as well.

It is not done yet, sometimes the firewalls will block connections between the LAN and the Internet. To overcome this, it is needed to create some rules for the firewall to permit these connections. Search for the advanced options with firewalls on your computer and, with it open, create two rules (one outbound and one inbound).

These rules must target "one application", "enable connections" and must target your MapleStory client (aka localhost).

After all these steps, the portforwarding process should now be complete.

---

---

---

# HeavenMS

Old sections left from the HeavenMS README that might still be relevant.

## Head developer, Ronan C. P. Lana

Besides myself for maintaining this repository, credits are to be given to Wizet/Nexon (owners of MapleStory & it's IP contents), the original MapleSolaxia staff and other colaborators, as just some changes/patches on the game were applied by myself, in which some of them diverged from the original v83 patch contents (alright, not just "some patches" by now since a whole lot of major server core changes have been applied on this development).

Regarding distributability and usage of the code presented here: like it was before, this MapleStory server is open-source. By that, it is meant that anyone is **free to install, use, modify and redistribute the contents**, as long as there is **no kind of commercial trading involved** and the **credits to the original creators are maintained** within the codes.

In this project, many gameplay-wise issues generated from either the original WZ files and the server source have been partially or completely solved. Considering the use of the provided edited WZ's and server-side wz.xml files should be of the greatest importance when dealing with this instance of server source, in order to perceive it at it's full potential. My opinion, though!

- In other case, as fallback from the provided ones, consider using **whole clean set**. Selecting part of the provided ones to play pretty much *may eventually* lead to unexpected issues.

The main objective of this effort is to try as best as possible to recreate what once was the original MapleStory v83, while adding up some flavors that spices up the gameplay. In other words, aim to get the best of the MapleStory of that era.

---

#### Mission

With non-profitting means intended, provide nostalgic pre-BB maplers world-wide a quality local server for freestyle entertainment.

#### Vision

By taking the v83 MapleStory as the angular stone, incrementally look forward to improve the gaming experience whilst still retaining the "clean v83" conservative ideal. Also, through reviewing distinguished aspects of the server's behavior that could be classified as a potential server threat, in the long run look for ways to improve or even stabilize some of it's uncertain aspects.

#### Values

* Autonomy, seek self-improvement for tackling issues head-on;
* Adventurous, take no fear of failures on the path of progress;
* Light-hearted support, general people out there didn't experience what you've already had;
* Humility, no matter how good you are, there's no good in boasting yourself over experiences only a few have had.

---

#### The MobBookUpdate example

As an example of client WZ editing, consider the MapleMobBookUpdate tool project I developed, it updates all reported drop data on the Monster Book with what is currently being hold on the database:

To make it happen:

* Open the MobBookUpdate project on NetBeans, located at "tools\MapleMobBookUpdate", and build it.
* At the subfolder "lib", copy the file "MonsterBook.img.xml". This is from the original WZ v83.
* Paste it on the "dist" subfolder.
* Inside "dist", open the command prompt by alt+right clicking there.
* Execute "java -jar MobBookUpdate.jar". It will generate a "MonsterBook_updated.img.xml" file.
* At last, overwrite the "MonsterBook.img.xml" on "C:\Nexon\Cosmic\wz\String.wz" with this file, renaming it back to "MonsterBook.img.xml".

At this point, **just the server-side** Monster Book has been updated with the current state of the database's drop data.

To **update the client as well**, open HaRepacker 4.2.2 and load "String.wz" from "C:\Nexon\MapleStory". Drop the "MonsterBook.img" node by removing it from the hierarchy tree, then import the server's "MonsterBook.img.xml".

**Note:** On this case, a server-to-client data transfer has been instanced. This kind of action **could cause** problems on the client-side if done unwary, however the nodes being updated on client-side and server-side provides no conflicts whatsoever, so this is fine. Remember, server-to-client data reimport may be problematic, whereas client-to-server data reimport is fine.

The client's WZ now has the proper item drops described by the DB updated into the MobBook drop list.

**Save the changes and overwrite the older WZ** on the MapleStory client folder.

---

#### Announcements

HeavenMS development as we can see right now achieved an acceptable state-of-the-art. A heartfelt thanks for everyone that contributed in some way for the progress of this server!

A case study has been conducted with the objective of overview results achieved during HeavenMS development. Those can be checked out on: https://heavenms-survey.home.blog/2019/12/24/project-visualizations/

---
### Open-source client development - HeavenClient

Continuing from where **SYJourney**'s JourneyClient has finished contributions (as of 5 Jul 2016), an open-source development of a software artifact designed to handle both gaming operations and interactions with the server is being conducted.

Newer implementations are being maintained by **頼晏 (ryantpayton)** and aims to offer higher display resolution, bring recent UI contents for the pre-BB gameplay and incremental support on overall gaming perspective.

HeavenClient GitHub: https://github.com/ryantpayton/HeavenClient

#### Support HeavenMS

If you liked this project, please don't forget to __star__ the repo ;) .

It's never enough to tell this, thanks to everyone that have been contributing something for the continuous improvement of the server! Be it through bug reports, donation, code snippets and/or pull requests.

Our Discord channel is still available on: https://discord.gg/Q7wKxHX

<hr id="donate" />

### Disclaimer

* HeavenMS staff has __no current intention__ to publicly open a server with this source, if that ever comes to happen this note will be lifted. __Don't be scammed!__

* This server source is __NOT intended to be stable__ as is. Proper deadlock review and other maintenance contributions are needed in order to make it steps ahead on viability.
