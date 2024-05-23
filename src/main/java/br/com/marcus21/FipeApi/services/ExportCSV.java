package br.com.marcus21.FipeApi.services;

import br.com.marcus21.FipeApi.models.Veiculo;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ExportCSV {
    public void exportarVeiculosParaCsv(List<Veiculo> veiculos) {
        String csvFile = "D:\\projetos\\Spring\\Fipe-Api\\csv_veiculos\\veiculos.csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile))) {
            String[] header = {"Código", "Marca", "Modelo", "Ano", "Preço"};
            writer.writeNext(header);

            for (Veiculo veiculo : veiculos) {
                String[] data = {
                        veiculo.valor(),
                        veiculo.marca(),
                        veiculo.modelo(),
                        veiculo.tipoCombustivel(),

                };
                writer.writeNext(data);
            }

            System.out.println("A lista de veículos foi exportada para " + csvFile);
        } catch (IOException e) {
            System.err.println("Erro ao exportar para CSV: " + e.getMessage());
        }
    }
}
