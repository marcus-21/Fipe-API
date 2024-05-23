package br.com.marcus21.FipeApi.start;

import br.com.marcus21.FipeApi.models.Dados;
import br.com.marcus21.FipeApi.models.Modelos;
import br.com.marcus21.FipeApi.models.Veiculo;
import br.com.marcus21.FipeApi.services.ConsumoAPI;
import br.com.marcus21.FipeApi.services.ConverteDados;
import br.com.marcus21.FipeApi.services.ExportCSV;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Start {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private final String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private ConverteDados conversor = new ConverteDados();
    private ExportCSV csv = new ExportCSV();

    public void exibirMenu(){
        var menu = """
                *** Opções de Escolha ***
                    1-Carro
                    2-Moto
                    3-Caminhão
                    
                Escolha uma das opções para que você possa consultar:
                """;

        System.out.println(menu);
        var opcao = scanner.nextLine();
        String option;
        switch (opcao){
            case "1":
                option = ENDERECO + "carros/marcas";
                break;
            case "2":
                option = ENDERECO + "motos/marcas";
                break;
            case "3":
                option = ENDERECO + "caminhoes/marcas";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + opcao);

        }
        String json = consumoAPI.obterDados(option);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted (Comparator.comparing (Dados::codigo))
        .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta:");
        var codigoMarca = scanner.nextLine();
        option = option + "/" +codigoMarca + "/modelos";
        json = consumoAPI.obterDados(option);
        var modeloLista  = conversor.obterDados(json, Modelos.class);
        System.out.println("\nModelos dessa Marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);


        System.out.println("\nDigite uma parte do nome do veiculo para ser consultado os seus modelos:");
        var nomeCarro = scanner.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream().filter(m -> m.nome().toUpperCase().contains(nomeCarro.toUpperCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos Filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o codigo do modelo para buscar a avaliação");
        var codigoDoModelo = scanner.nextLine();

        option = option + "/" + codigoDoModelo + "/anos";

        json = consumoAPI.obterDados(option);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (var i = 0; i < anos.size(); i++ ){
            var endpointAnosVeiculos = option + "/" + anos.get(i).codigo();
            json = consumoAPI.obterDados(endpointAnosVeiculos);
            Veiculo veiculo = conversor.obterDados(json , Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\n Lista de todos os veiculos valiados por ano:");
        veiculos.forEach(System.out::println);

        System.out.println("Deseja exportar a lista de veículos para CSV? (s/n)");
        var exportarCsv = scanner.nextLine();
        if (exportarCsv.equalsIgnoreCase("s")) {
            csv.exportarVeiculosParaCsv(veiculos);
        }
    }




}
