package arollavengers.core.domain.pandemic;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class PlayerDrawCard {

    private final List<PlayerCard> cards;

    public PlayerDrawCard() {
        this.cards = Collections.synchronizedList(Lists.<PlayerCard>newArrayList());
    }

    public void buildAndShuffle() {
        Collections.addAll(cards, PlayerCityCard.values());
        Collections.shuffle(cards);
    }

    public PlayerCard drawTop() {
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }
}
