from extrator_url import ExtratorUrl

if __name__ == "__main__":
    url = "https://bytebank.com/cambio?quantidade=100&moedaOrigem=real&moedaDestino=dolar"

    extrator = ExtratorUrl(url)
    print(extrator.get_valor_parametro('quantidade'))
