package online.omnia.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lollipop on 05.10.2017.
 */
@Table(name = "adverts")
@Entity
public class AdvertsEntity {
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "advshortname")
    private String advName;

    public int getId() {
        return id;
    }

    public String getAdvName() {
        return advName;
    }

}
