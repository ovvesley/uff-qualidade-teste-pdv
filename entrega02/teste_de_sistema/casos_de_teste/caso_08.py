#Criar e editar fornecedeor


from helpers.usuario import MENU, login

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from selenium.common.exceptions import TimeoutException

driver = webdriver.Chrome()


def cria_fornecedor(nomeFantasia, Nome, CNPJ, Escricao, Observacao, rua, bairro, numero, cep, referencia, telefone):
     
    time.sleep(5)

    campo_fornecedor = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[9]/a/img').click()

    time.sleep(5)

    campo_NovoFornecedor = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a').click()
    nomeFantasia = driver.find_element(By.XPATH, '//*[@id="nomefantasia"]').send_keys(nomeFantasia)

    time.sleep(5)

    campo_nome = driver.find_element(By.XPATH, '//*[@id="nome"]').send_keys(Nome)

    time.sleep(5)

    campo_cnpj = driver.find_element(By.XPATH, '//*[@id="cnpj"]').send_keys(CNPJ)

    time.sleep(5)

    campo_escricao = driver.find_element(By.XPATH, '//*[@id="escricao"]').send_keys(Escricao)

    time.sleep(5)

    campo_listaAtivo = driver.find_element(By.XPATH, '//*[@id="situacao"]').click()
    campo_ativo = driver.find_element(By.XPATH, '//*[@id="situacao"]/option[1]').click()

    time.sleep(5)

    campo_obs = driver.find_element(By.XPATH, '//*[@id="form_fornecedor"]/ul/li[1]/a').click()
    campo_obsTexto = driver.find_element(By.XPATH, '//*[@id="observacao"]').send_keys(Observacao)

    time.sleep(5)

    element = WebDriverWait(driver, 10).until(
    EC.visibility_of_element_located((By.XPATH, '//*[@id="form_fornecedor"]/ul/li[2]/a'))
)
    element.click()

    try:
        listaendCidade = WebDriverWait(driver, 10).until(
        EC.visibility_of_element_located((By.XPATH, '//*[@id="cidade"]'))
    )
    except Exception:
        print("O campo listaendCidade não foi encontrado no tempo esperado.")
        assert listaendCidade.is_displayed(), "O campo listaendCidade não está visível."

    campo_endCidade = driver.find_element(By.XPATH, '//*[@id="cidade"]/option[1]').click()
    campo_endRua = driver.find_element(By.XPATH, '//*[@id="rua"]').send_keys(rua)
    campo_endBairro = driver.find_element(By.XPATH, '//*[@id="bairro"]').send_keys(bairro)
    campo_endNumero = driver.find_element(By.XPATH, '//*[@id="numero"]').send_keys(numero)
    campo_endCEP = driver.find_element(By.XPATH, '//*[@id="cep"]').send_keys(cep)
    campo_endRef = driver.find_element(By.XPATH, '//*[@id="referencia"]').send_keys(referencia)

    time.sleep(5)

    element = WebDriverWait(driver, 10).until(
    EC.visibility_of_element_located((By.XPATH, '//*[@id="form_fornecedor"]/ul/li[3]/a'))
)
    element.click()


    try:
        campo_tel = WebDriverWait(driver,10).until(
        EC.visibility_of_element_located((By.XPATH, '//*[@id="fone"]'))
    )
        campo_tel = WebDriverWait(driver, 10).until(
       
    )
    except Exception:
        print("O campo não foi encontrado no tempo esperado.")
        assert campo_tel.is_displayed(), "o campo telefone não foi encontrado"
    
    cmapo_tel = driver.find_element(By.XPATH, '//*[@id="fone"]').send_keys(telefone)

    campo_listaFixo = driver.find_element(By.XPATH, '//*[@id="tipo"]').click()
    campo_fixo = driver.find_element(By.XPATH, '//*[@id="tipo"]/option[1]'). click()

    time.sleep(5)
    campo_salvar = driver.find_element(By.XPATH, '//*[@id="form_fornecedor"]/input').click()

    time.sleep(5)

    campo_listar = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a').click()

    try:
        WebDriverWait(driver, 10).until(
            EC.visibility_of_element_located((By.XPATH, f'//table/tbody/tr/td[contains(text(), "{CNPJ}")]'))
        )
        assert driver.find_element(By.XPATH, f'//table/tbody/tr/td[contains(text(), "{CNPJ}")]')
        print(f"Fornecedor com CNPJ '{CNPJ}' adicionado com sucesso.")

    except TimeoutException:
        print(f"Fornecedor com CNPJ '{CNPJ}' não apareceu na lista a tempo.")
        raise AssertionError(f"CNPJ '{CNPJ}' não encontrado na lista de fornecedores.")


try:
    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)


    cria_fornecedor(
            nomeFantasia = "empresa teste",
            Nome = "teste",
            CNPJ = "19.526.979/0002-45",
            Escricao = 12,
            Observacao = "teste",
            rua = "rua teste",
            bairro = "bairro teste",
            numero = 10,
            cep = 23242222,
            referencia = "rua dos bobos numero zero",
            telefone = 21999999
        )

    
finally:
    driver.quit()