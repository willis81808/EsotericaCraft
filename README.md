<p align="center">
  <img src="https://i.imgur.com/CG7Wf6I.png">
</p>

# Introduction
This mod introduces a new hard magic system built to maintain a Vanilla-like feel to the game. Runes can be built in the world out of different patterns of Mundane and Enchanted blocks, and when activated with a "Runing Staff" or via redstone have varied useful effects ranging from enabling teleportation between distant locations, protecting your base from griefing and hostile mob spawning, to trapping to souls of mobs, and more!

In addition to the mystical runes, this mod introduces a "sleep vote" system (configurable in-game by operators via the /sleepvote command), and a "chest shop" system whereby players can protect chests from theft as well as automatically facilitate trade without the owner having to rely on trust or even needing to be present for the transaction at all!

# Crafting Runes

[none]: https://i.imgur.com/rHk2Ifb.png
[mundane]: https://i.imgur.com/OCVBBV9.png
[enchanted]: https://i.imgur.com/PnYfGrY.png
[other]: https://i.imgur.com/fyNVr4W.png

## Tiers

Runes are created by placing Rune Materials (a.k.a blocks) in specific patterns before activating them with a Runing Staff. Every blocks falls into one of three tiers:

| Enchanted | &nbsp; Mundane &nbsp; | &nbsp;&nbsp;&nbsp; None &nbsp;&nbsp;&nbsp; |
| --- | --- | --- |
| &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![tier][enchanted] | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![tier][mundane] | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;![tier][none] |

Lists of each block type can be found here:

[Mundane Blocks List](https://willis81808.github.io/EsotericaCraft/)

[Enchanted Block List](https://willis81808.github.io/EsotericaCraft/)

`None` tier is any block that isn't Mundane or Enchanted (e.g. air, grass, torches, liquids, furnaces, and many others)

## Recipes

### Teleport Receiver:

| ![tier][none] | ![tier][mundane] | ![tier][mundane] | ![tier][mundane] | ![tier][none] |
| --- | --- | --- | --- | --- |
| ![tier][mundane]  | ![tier][mundane]  | ![tier][enchanted] | ![tier][mundane] | ![tier][mundane] |
| ![tier][mundane]  | ![tier][enchanted]  | ![tier][none] | ![tier][enchanted] | ![tier][mundane] |
| ![tier][mundane]  | ![tier][mundane]  | ![tier][enchanted] | ![tier][mundane] | ![tier][mundane] |
| ![tier][none]  | ![tier][mundane]  | ![tier][mundane] | ![tier][mundane] | ![tier][none] |

> Note: The four Enchanted blocks used in this rune are a unique signature. The combination of blocks used, as well as their orientation (north, south, east, west) must match the Teleport Transmitter you wish this Rune to teleport to.

### Teleport Transmitter:

| ![tier][none] | ![tier][mundane] | ![tier][enchanted] | ![tier][mundane] | ![tier][none] |
| --- | --- | --- | --- | --- |
| ![tier][mundane]  | ![tier][mundane]  | ![tier][mundane] | ![tier][mundane] | ![tier][mundane] |
| ![tier][enchanted]  | ![tier][mundane]  | ![tier][none] | ![tier][mundane] | ![tier][enchanted] |
| ![tier][mundane]  | ![tier][mundane]  | ![tier][mundane] | ![tier][mundane] | ![tier][mundane] |
| ![tier][none]  | ![tier][mundane]  | ![tier][enchanted] | ![tier][mundane] | ![tier][none] |

> Note: The four Enchanted blocks used in this rune are a unique signature. The combination of blocks used, as well as their orientation (north, south, east, west) must match the Teleport Receiver you wish this Rune to teleport to.

### Dampen:

| ![tier][none] | ![tier][enchanted] | ![tier][enchanted] | ![tier][enchanted] | ![tier][none] |
| --- | --- | --- | --- | --- |
| ![tier][enchanted]  | ![tier][none]  | ![tier][mundane] | ![tier][none] | ![tier][enchanted] |
| ![tier][enchanted]  | ![tier][mundane]  | ![tier][mundane] | ![tier][mundane] | ![tier][enchanted] |
| ![tier][enchanted]  | ![tier][none]  | ![tier][mundane] | ![tier][none] | ![tier][enchanted] |
| ![tier][none]  | ![tier][enchanted]  | ![tier][enchanted] | ![tier][enchanted] | ![tier][none] |

> Note: This rune requires that all its Enchanted blocks be either Diamond or Emerald blocks, and all Mundane blocks used in the pattern must be the same block.

### Recall:

| ![tier][none] | ![tier][other] | ![tier][other] | ![tier][other] | ![tier][none] |
| --- | --- | --- | --- | --- |
| ![tier][other]  | ![tier][none]  | ![tier][other] | ![tier][none] | ![tier][other] |
| ![tier][other]  | ![tier][other]  | ![tier][enchanted] | ![tier][other] | ![tier][other] |
| ![tier][other]  | ![tier][none]  | ![tier][other] | ![tier][none] | ![tier][other] |
| ![tier][none]  | ![tier][other]  | ![tier][other] | ![tier][other] | ![tier][none] |

> Note: Center enchanted block MUST be a Redstone Block. The rest must be Redstone Dust/Wire.
