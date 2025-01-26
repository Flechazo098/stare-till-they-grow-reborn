# Stare Till They Grow Reborn

## 更新日志

### 3.0.0 版本更新
- 修复了村民补货系统的问题：
  1. 优化了商人补货的冷却时间管理，使用ConcurrentHashMap存储每个商人的上次补货时间
  2. 增加了智能补货机制：
     - 当商人所有交易都被买断后，看着商人就会立即补货
     - 补货后进入60秒冷却时间
     - 60秒冷却结束后，再次看着商人可以触发补货
  3. 改进了商人交易状态的管理和恢复，支持单个交易项的状态跟踪
  4. 增加了详细的日志记录，方便调试和监控
  5. 优化了代码结构，提高了可维护性

### 主要功能
- 通过注视村民来触发村民补货
- 智能补货系统：
  - 所有交易买断后立即补货
  - 60秒补货冷却时间
  - 冷却结束后可再次补货
- 支持所有类型的商人（村民和流浪商人）

### 配置说明
- 补货冷却时间：60秒
- 日志级别：可在配置文件中调整

### 使用说明
1. 安装mod后，只需要注视村民即可触发补货
2. 补货条件：
   - 商人的所有交易都已买断
   - 不在60秒冷却时间内
3. 补货后进入60秒冷却时间
4. 冷却时间结束后可再次补货

### 注意事项
- 补货功能需要所有交易都买断才能触发
- 补货后有60秒冷却时间
- 重新打开交易界面不会重置已买断物品的状态

### 技术细节
- 使用ConcurrentHashMap确保线程安全
- 实现了完整的事件处理系统
- 优化了性能和内存使用
- 支持单个交易项的状态管理

### 贡献者
- Flechazo（主要开发者）

### 许可证
- MIT License

Literally look at your plants to make them grow!

A mod inspired by [Twerk Sim 2K16](https://github.com/Funwayguy/TS2K16) by [Funwayguy](https://github.com/Funwayguy).

### Preview video (click image)
[![Stare Till They Grow!](https://img.youtube.com/vi/8oO2xrJ-KZU/0.jpg)](https://www.youtube.com/watch?v=8oO2xrJ-KZU)

## features

Works with any bonemeal-able plants. With configurable delay and apply speed.

Includes blacklist and whitelist options. 

## Permissions
You are free to use this mod in any modpack, You must also be abiding by Minecraft's EULA.

## In depth settings

### shiftToActivate (default: false)
Only enable this mod when bending over (holding shift).

### applyBoneMeal (default: true)
If staring at plants applies bone meal.

### growBabies (default: true)
If staring at baby animals makes them grow.

### regrowWool (default: true)
If staring at sheared sheep makes their wool grow back.

### Delay (default: 2)
The delay in seconds before we start applying bonemeal.

### Every x Seconds (default: 1)
The time in seconds between each applying of bonemeal.

### Use black or whitelist (default: blacklist)
You can choose to use either a blacklist or a whitelist.

### Blacklist (default: [minecraft:grass_block])
Disallow items for being stared at.

### Whitelist (default: [])
Only allow certain items to be stared at.

