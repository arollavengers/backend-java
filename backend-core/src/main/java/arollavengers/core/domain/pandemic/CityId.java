package arollavengers.core.domain.pandemic;

/**
 * @author <a href="http://twitter.com/aloyer">@aloyer</a>
 */
@SuppressWarnings("UnusedDeclaration")
public enum CityId {
    Algiers(Disease.Black),
    Baghdad(Disease.Black),
    Cairo(Disease.Black),
    Chennai(Disease.Black),
    Delhi(Disease.Black),
    Istanbul(Disease.Black),
    Karachi(Disease.Black),
    Kolkata(Disease.Black),
    Moscow(Disease.Black),
    Mumbai(Disease.Black),
    Riyadh(Disease.Black),
    Tehran(Disease.Black),
    // ~~~
    Atlanta(Disease.Blue),
    Chicago(Disease.Blue),
    Essen(Disease.Blue),
    London(Disease.Blue),
    Madrid(Disease.Blue),
    Milan(Disease.Blue),
    NewYork(Disease.Blue),
    Paris(Disease.Blue),
    SanFrancisco(Disease.Blue),
    SaintPetersburg(Disease.Blue),
    Toronto(Disease.Blue),
    Washington(Disease.Blue),
    // ~~~
    Bangkok(Disease.Orange),
    Beijing(Disease.Orange),
    HoChiMinhCity(Disease.Orange),
    HongKong(Disease.Orange),
    Jakarta(Disease.Orange),
    Manila(Disease.Orange),
    Osaka(Disease.Orange),
    Seoul(Disease.Orange),
    Shanghai(Disease.Orange),
    Sydney(Disease.Orange),
    Taipei(Disease.Orange),
    Tokyo(Disease.Orange),
    // ~~~
    Bogota(Disease.Yellow),
    BuenosAires(Disease.Yellow),
    Johannesburg(Disease.Yellow),
    Khartoum(Disease.Yellow),
    Kinshasa(Disease.Yellow),
    Lagos(Disease.Yellow),
    Lima(Disease.Yellow),
    LosAngeles(Disease.Yellow),
    MexicoCity(Disease.Yellow),
    Miami(Disease.Yellow),
    Santiago(Disease.Yellow),
    SaoPaulo(Disease.Yellow)
    ;

    private final Disease disease;
    CityId(Disease disease) {
        this.disease = disease;
    }

    public Disease defaultDisease() {
        return disease;
    }

    public static int nbCities() {
        return values().length;
    }
}
