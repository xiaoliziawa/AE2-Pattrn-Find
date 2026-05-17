# AE2 Pattern Find

A small add-on for Applied Energistics 2 that points out where a pattern lives.

If your base has dozens of pattern providers and you can't remember which one runs a given recipe, this mod adds a quick locate action. Open the Pattern Access Terminal, hold Shift+Alt on a pattern, and the block gets highlighted in the world.

## Usage

1. Open a **Pattern Access Terminal** (block or part — both work).
2. Hover the cursor over the pattern slot you want to find.
3. Hold **Shift + Alt**. A small ring fills around the cursor.
4. When the ring completes, the terminal closes and the pattern provider gets outlined in the world.

The outline is drawn through walls, so the block stays visible even if it's behind a few layers of concrete. An arrow orbits the crosshair when the block is off-screen, so you know which way to turn.

The marker fades after a configurable timeout (30 seconds by default). Multiple markers can stay up at once.

## Configuration

Config file: `config/ae2_pattern_find-common.toml`

| Key | Default | Meaning |
| --- | --- | --- |
| `loading.loadTicks` | 10 | Ticks of Shift+Alt required to fire (20 = 1 second) |
| `marker.highlightTicks` | 600 | How long the marker stays visible |
| `marker.color` | `#00FFFF` | Hex color, applied to outline, label, ring and arrow |
| `marker.lineWidth` | 3.0 | Outline thickness in pixels |
| `marker.showDistanceText` | true | Show pattern name and distance above the block |
| `arrow.showArrow` | true | Show on-screen arrow when the block is off-screen |
| `arrow.radius` | 40 | Arrow distance from the crosshair, in screen pixels |

## Compatibility

Tested with:

- **AE2** 15.x — both the block form and the part form of Pattern Providers
- **ExtendedAE** — Assembler Matrix is located by its pattern panel block
- **AdvancedAE** — Advanced Pattern Providers (block and part, both sizes)

Other add-ons should work automatically as long as their pattern container exposes a `getBlockEntity()` method. No hard-coded list, so you don't need to wait for a mod update when a new AE2 add-on appears.

Cross-dimension patterns are not drawn in the world — instead, the dimension name and coordinates are printed to chat.

## Requirements

- Minecraft **1.20.1**
- Forge **47.x**
- Applied Energistics 2 **15.0+**
- Required on **both client and server**

## In-game guide

A short usage page is added to AE2's built-in guidebook (Pattern Access Terminal section). You can open the guidebook from any AE2 item that has the "Open Guide" button.

## License

GPL-3.0

---

## 简介（中文）

Applied Energistics 2 的附属小模组，用来在世界中标出某个样板所在的样板供应器。

如果你的基地有几十个样板供应器，记不住哪个跑哪个配方，这个 mod 加了一个快速定位操作：

1. 打开**样板管理终端**（方块或线缆部件都行）
2. 鼠标悬停到你要找的样板上
3. 按住 **Shift + Alt**，光标周围会有一个圈逐渐填满
4. 圈满后界面自动关闭，目标方块在世界中被高亮

高亮线框可以穿透方块，被墙挡住也能看见。方块在视野外时，准星周围会出现一个箭头指向它。高亮持续 30 秒后消失（可在配置中调整）。

支持原版 AE2，已测试兼容 **ExtendedAE** 的装配矩阵和 **AdvancedAE** 的高级样板供应器。其它附属只要其 PatternContainer 实现暴露了 `getBlockEntity()` 方法就会自动支持。

服务器和客户端**都需要安装**这个模组。
