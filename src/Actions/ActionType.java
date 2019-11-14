
package Actions;

/**
 * All of the current types of actions in the game.
 * 
 * @author Brion W. Lang
 * Date: 17 Jan 2009
 * Version 1.1.0
 * 1. Added the "seeportal" action.
 * Version: 1.0.0
 */
public enum ActionType {
addlogic,
announce,
attemptdropitem,
attemptenterportal,
attemptgetitem,
attemptgiveitem,
attemptsay,
attempttransport,
candropitem,//Query Actions
canenterportal,//Query Actions
canenterregion,//Query Actions
canenterroom,//Query Actions
cangetitem,//Query Actions
canleaveregion,//Query Actions
canleaveroom,//Query Actions
canreceiveitem,//Query Actions
cansay,//Query Actions
chat,
cleanup,
command,
dellogic,
destroycharacter,
destroyitem,
doaction,
dropitem,
enterportal,
enterrealm,
enterregion,
enterroom,
error,
getitem,
giveitem,
hangup,
leave,
leaverealm,
leaveregion,
leaveroom,
messagelogic,
modifyattribute,
query,//Query Actions
reloadcharacters,
reloadcommandscript,
reloaditems,
reloadlogicscript,
reloadregion,
savedatabases,
saveplayers,
saveregion,
savetimers,
say,
spawncharacter,
spawnitem,
transport,
vision,
//new ones
shutdown,
seeroom,
forcetransport,
seeroomname,
seeroomdescription,
seeroomexits,
seeroomcharacters,
seeroomitems,
whisper,
die,
//below are added for functionality of my own.
seeitem, seemob, spell, seeportal;

}
