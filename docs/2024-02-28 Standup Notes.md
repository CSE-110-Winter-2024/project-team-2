# Week 8 Wednesday 2/28

## Attendees
Andy Huynh, Benjamin Johnson, Hannah Coates, Keisuke Hirano

## Updates

Andy Huynh + Hannah Coates
- What you’ve been working on: N/A
- Blockers: N/A
- Goals for next standup: going to start US11 some time after US10 is done

Benjamin Johnson + Tianlin Zhao
- What you’ve been working on: N/A
- Blockers: N/A
- Goals for next standup: going to start US14 by Friday

Keisuke Hirano + Vivian Liu
- What you’ve been working on: making good progress on US10, hoping to finish by tonight. Some discussion on how to represent a goal's list & date
- Blockers: N/A
- Goals for next standup: meeting later today, aim to get a PR up by EOD Friday

Vivian's asynchronous updates:
- Keisuke and I met last night to work on US10 and changes are on branch US10-choose-goal-view
- Made viewOptions enum
- We made an arrow in the upper right corner and a dialog comes up when you tap it. You can now change between different views and the title at the top changes accordingly
  - Note: we had to truncate the day of the week to the first 3 letters or else it didn't always fit in the app bar
- No goals message now only shows up on today's view not any other views
- Added 2 fields in GoalEntity: goalDate and isPending
- Goals for next standup:
  - Implement filtering depending on the view
  - Make sure goals are added correctly when on tomorrow or pending views
  - If time: testing. adding 2 fields broke a lot of our previous tests lol so we need to fix that

## Notes

- No weekly meeting this Friday, just standup (some ppl can't make it + doesn't feel necessary)
