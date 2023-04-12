package dk.tandhjulet.enums;

public enum BandeRank {
    EJER(4),
    ADMIN(3),
    MOD(2),
    MEDLEM(1);

    private Integer priority;

    BandeRank(Integer priority) {
        this.priority = priority;
    }

    public boolean isHigherThan(BandeRank other) {
        return this.priority > other.priority;
    }

    public boolean isHigherThanOrEqualTo(BandeRank other) {
        return this.priority >= other.priority;
    }

    public boolean isLowerThan(BandeRank other) {
        return this.priority < other.priority;
    }

    public boolean isLowerThanOrEqualTo(BandeRank other) {
        return this.priority <= other.priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public BandeRank next() {
        switch (this.priority) {
            case 1:
                return BandeRank.MOD;
            case 2:
                return BandeRank.ADMIN;
            case 3:
                return BandeRank.ADMIN;
            case 4:
                return BandeRank.EJER;
        }
        return null;
    }

    public BandeRank prev() {
        switch (this.priority) {
            case 1:
                return BandeRank.MEDLEM;
            case 2:
                return BandeRank.MEDLEM;
            case 3:
                return BandeRank.MOD;
            case 4:
                return BandeRank.EJER;
        }
        return null;
    }
}