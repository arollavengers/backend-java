package arollavengers.core.domain;

import static arollavengers.core.domain.Disease.Black;
import static arollavengers.core.domain.Disease.Blue;
import static arollavengers.core.domain.Disease.Orange;
import static arollavengers.core.domain.Disease.Yellow;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@SuppressWarnings("UnusedDeclaration")
public enum CityId {
    Algiers(Black),
    Baghdad(Black),
    Cairo(Black),
    Chennai(Black),
    Delhi(Black),
    Istanbul(Black),
    Karachi(Black),
    Kolkata(Black),
    Moscow(Black),
    Mumbai(Black),
    Riyadh(Black),
    Tehran(Black),
    // ~~~
    Atlanta(Blue),
    Chicago(Blue),
    Essen(Blue),
    London(Blue),
    Madrid(Blue),
    Milan(Blue),
    NewYork(Blue),
    Paris(Blue),
    SanFrancisco(Blue),
    SaintPetersburg(Blue),
    Toronto(Blue),
    Washington(Blue),
    // ~~~
    Bangkok(Orange),
    Beijing(Orange),
    HoChiMinhCity(Orange),
    HongKong(Orange),
    Jakarta(Orange),
    Manila(Orange),
    Osaka(Orange),
    Seoul(Orange),
    Shanghai(Orange),
    Sydney(Orange),
    Taipei(Orange),
    Tokyo(Orange),
    // ~~~
    Bogota(Yellow),
    BuenosAires(Yellow),
    Johannesburg(Yellow),
    Khartoum(Yellow),
    Kinshasa(Yellow),
    Lagos(Yellow),
    Lima(Yellow),
    LosAngeles(Yellow),
    MexicoCity(Yellow),
    Miami(Yellow),
    Santiago(Yellow),
    SaoPaulo(Yellow)
    ;

    private final Disease disease;
    CityId(Disease disease) {
        this.disease = disease;
    }

    public Disease defaultDisease() {
        return disease;
    }
}
