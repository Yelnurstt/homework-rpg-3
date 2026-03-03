package com.narxoz.rpg.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BattleEngine {
    private static BattleEngine instance;
    private Random random = new Random(1L);

    private BattleEngine() {
    }

    public static BattleEngine getInstance() {
        if (instance == null) {
            instance = new BattleEngine();
        }
        return instance;
    }

    public BattleEngine setRandomSeed(long seed) {
        this.random = new Random(seed);
        return this;
    }

    public void reset() {
        this.random = new Random(1L);
    }

    public EncounterResult runEncounter(List<Combatant> teamA, List<Combatant> teamB) {
        EncounterResult result = new EncounterResult();

        if (teamA == null || teamB == null || teamA.isEmpty() || teamB.isEmpty()) {
            result.setWinner("None (Invalid Teams)");
            return result;
        }

        List<Combatant> a = new ArrayList<>(teamA);
        List<Combatant> b = new ArrayList<>(teamB);
        int rounds = 0;

        result.addLog("Battle starts! Team A (" + a.size() + ") vs Team B (" + b.size() + ")");

        while (!a.isEmpty() && !b.isEmpty()) {
            rounds++;
            result.addLog("--- Round " + rounds + " ---");

            // Team A attacks Team B
            performAttacks(a, b, result);
            if (b.isEmpty()) break;

            // Team B attacks Team A
            performAttacks(b, a, result);
        }

        result.setRounds(rounds);
        if (!a.isEmpty()) {
            result.setWinner("Team A");
        } else if (!b.isEmpty()) {
            result.setWinner("Team B");
        } else {
            result.setWinner("Draw");
        }

        return result;
    }

    private void performAttacks(List<Combatant> attackers, List<Combatant> defenders, EncounterResult result) {
        for (Combatant attacker : attackers) {
            if (defenders.isEmpty()) break;
            if (!attacker.isAlive()) continue;

            Combatant target = defenders.get(random.nextInt(defenders.size()));
            int damage = attacker.getAttackPower();
            target.takeDamage(damage);

            result.addLog(attacker.getName() + " attacks " + target.getName() + " for " + damage + " damage.");

            if (!target.isAlive()) {
                result.addLog(target.getName() + " has been defeated!");
                defenders.remove(target);
            }
        }
    }
}