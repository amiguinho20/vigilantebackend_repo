package br.com.fences.vigilantebackend.negocio;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderAddressComponent;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.code.geocoder.model.GeocoderResultType;
import com.google.code.geocoder.model.GeocoderStatus;
import com.google.code.geocoder.model.LatLng;

import br.com.fences.fencesutils.rest.tratamentoerro.exception.RestRuntimeException;
import br.com.fences.fencesutils.verificador.Verificador;
import br.com.fences.vigilanteentidade.negocio.Endereco;

@RequestScoped
public class MapaBO {

	private static final String IDIOMA = "pt-BR";
	private static final Geocoder geocoder = new Geocoder();

	public Endereco consultarEndereco(Double latitude, Double longitude)
	{
		Endereco endereco = new Endereco();
		endereco.setLatitude(latitude);
		endereco.setLongitude(longitude);
		try
		{
			LatLng latLng = new LatLng(Double.toString(latitude), Double.toString(longitude));
			GeocoderRequest geocoderRequest = new GeocoderRequestBuilder()
					.setLocation(latLng)
					.setLanguage(IDIOMA)
					.getGeocoderRequest();
			GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
			if (geocoderResponse.getStatus().equals(GeocoderStatus.OK))
			{
				List<GeocoderResult> results = geocoderResponse.getResults();
				if (Verificador.isValorado(results))
				{
					for (GeocoderResult result : results)
					{
						if (result.getTypes().contains(GeocoderResultType.STREET_ADDRESS.value()))
						{
							for (GeocoderAddressComponent addressComponent : result.getAddressComponents())
							{
								if (addressComponent.getTypes().contains(GeocoderResultType.ROUTE.value()))
								{
									endereco.setLogradouro(addressComponent.getLongName());
								}
								else if (addressComponent.getTypes().contains(GeocoderResultType.STREET_NUMBER.value()))
								{
									endereco.setNumero(addressComponent.getLongName());
								}
								else if (addressComponent.getTypes().contains(GeocoderResultType.SUBLOCALITY.value()))
								{
									endereco.setBairro(addressComponent.getLongName());
								}
								else if (addressComponent.getTypes().contains(GeocoderResultType.LOCALITY.value()))
								{
									endereco.setCidade(addressComponent.getLongName());
								}
								else if (addressComponent.getTypes().contains(GeocoderResultType.ADMINISTRATIVE_AREA_LEVEL_1.value()))
								{
									endereco.setUf(addressComponent.getShortName());
								}
								else if (addressComponent.getTypes().contains(GeocoderResultType.POSTAL_CODE.value()))
								{
									endereco.setCep(addressComponent.getLongName());
									endereco.getCep().replaceAll("\\D", "");
									if (endereco.getCep().length() == 5)
									{
										endereco.setCep(endereco.getCep() + "000");
									}
								}
							}
						}
					}
				}
			}
		
		}
		catch(Exception e)
		{
			throw new RestRuntimeException(3, "Erro no processamento do Geocode : " + e.getMessage());
		}

		return endereco;
	}

}
