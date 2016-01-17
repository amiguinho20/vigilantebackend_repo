package br.com.fences.vigilantebackend.rest;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class InputStreamParaJson {
	
	public static String converter(InputStream entrada)
	{
		StringBuilder json = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8));
			String line = null;
			while ((line = in.readLine()) != null) {
				json.append(line);
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro no parse de InputStream para Json");
		}
		return json.toString();
	}

}
