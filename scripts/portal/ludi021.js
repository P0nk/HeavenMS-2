/*
    Exit Portal
    Map: Hidden Street: Secret Passage (922000009)
    - Remove quest items from player's inventory (Mechanical Parts - 4031092)
    - Returns user to Toy Factory <Aparatus Room> - 220020600
    - Reactors are reset and shuffled when event instance is created - not here
*/

function enter(pi) {
    pi.removeAll(4031092);
    pi.warp(220020600);
    return true;
} 