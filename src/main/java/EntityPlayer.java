public class EntityPlayer extends Entity
{
    private String name;
    private int count;
    private int exp;

    public EntityPlayer(World world, String title, double posX, double posZ, int maxHealth, int health, int attackDamage, String name) {
        super(world, title, posX, posZ, false, maxHealth, health, attackDamage);
        this.name = name;
        this.count = 0;
        this.exp = 0;
        DatabaseUtils.insertPlayers(this);
    }

    public EntityPlayer(int id, World world, String title, double posX, double posZ, int maxHealth, int health, int attackDamage, String name) {
        super(id, world, title, posX, posZ, false, maxHealth, health, attackDamage);
        this.name = name;
        this.count = 0;
        this.exp = 0;
        DatabaseUtils.insertPlayers(this);
    }

    @Override
    public void update() {
        super.update();
        count++;
        if (health != maxHealth) {
            if (count % 2 == 0) {
                if (health < maxHealth) {
                    health++;
                }
            }
        }
        if(count%5==0) {
            this.setExp(GameServer.getInstance().getConfig().getDifficulty()*10 + exp);
        }
    }

    @Override
    public String toString() {
        return "EntityPlayer{" +
                "id=" + id +
                ", world=" + world +
                ", title='" + title + '\'' +
                ", posX=" + posX +
                ", posZ=" + posZ +
                ", distance=" + distance +
                ", agressive=" + agressive +
                ", maxHealth=" + maxHealth +
                ", health=" + health +
                ", attackDamage=" + attackDamage +
                ", target=" + target +
                ", name='" + name + '\'' +
                ", exp=" + exp + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setName(String name) {
        this.name = name;
    }
}
