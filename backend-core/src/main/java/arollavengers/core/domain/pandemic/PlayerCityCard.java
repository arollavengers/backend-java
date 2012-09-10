package arollavengers.core.domain.pandemic;

public enum PlayerCityCard implements PlayerCard {
    Algiers(CityId.Algiers),
    Baghdad(CityId.Baghdad),
    Cairo(CityId.Cairo),
    Chennai(CityId.Chennai),
    Delhi(CityId.Delhi),
    Istanbul(CityId.Istanbul),
    Karachi(CityId.Karachi),
    Kolkata(CityId.Kolkata),
    Moscow(CityId.Moscow),
    Mumbai(CityId.Mumbai),
    Riyadh(CityId.Riyadh),
    Tehran(CityId.Tehran),
    // ~~~
    Atlanta(CityId.Atlanta),
    Chicago(CityId.Chicago),
    Essen(CityId.Essen),
    London(CityId.London),
    Madrid(CityId.Madrid),
    Milan(CityId.Milan),
    NewYork(CityId.NewYork),
    Paris(CityId.Paris),
    SanFrancisco(CityId.SanFrancisco),
    SaintPetersburg(CityId.SaintPetersburg),
    Toronto(CityId.Toronto),
    Washington(CityId.Washington),
    // ~~~
    Bangkok(CityId.Bangkok),
    Beijing(CityId.Beijing),
    HoChiMinhCity(CityId.HoChiMinhCity),
    HongKong(CityId.HongKong),
    Jakarta(CityId.Jakarta),
    Manila(CityId.Manila),
    Osaka(CityId.Osaka),
    Seoul(CityId.Seoul),
    Shanghai(CityId.Shanghai),
    Sydney(CityId.Sydney),
    Taipei(CityId.Taipei),
    Tokyo(CityId.Tokyo),
    // ~~~
    Bogota(CityId.Bogota),
    BuenosAires(CityId.BuenosAires),
    Johannesburg(CityId.Johannesburg),
    Khartoum(CityId.Khartoum),
    Kinshasa(CityId.Kinshasa),
    Lagos(CityId.Lagos),
    Lima(CityId.Lima),
    LosAngeles(CityId.LosAngeles),
    MexicoCity(CityId.MexicoCity),
    Miami(CityId.Miami),
    Santiago(CityId.Santiago),
    SaoPaulo(CityId.SaoPaulo);

    private final CityId cityId;

    PlayerCityCard(final CityId cityId) {
        this.cityId = cityId;
    }

    public CityId cityId() {
        return cityId;
    }
}
