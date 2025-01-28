# Criar e editar pessoa


from helpers.usuario import MENU, login, hover_lateral_menu

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions   EC
import time
from selenium.common.exceptions import TimeoutException, UnexpectedAlertPresentException

driver = webdriver.Chrome()


def cria_pessoa(nome, apelido, cpf, data_nascimento, observacao, endereco, contato):
    try : 
        nome = driver.find_element(By.ID, "nome").send_keys(nome)
        apelido = driver.find_element(By.ID, "apelido").send_keys(apelido)
        cpf = driver.find_element(By.ID, "cpfcnpj").send_keys(cpf)
        data_nascimento = driver.find_element(By.ID, "nascimento").send_keys(data_nascimento)
        observacao = driver.find_element(By.ID, "observacao").send_keys(observacao)

        # adicionando endereço
        endereco_campo = driver.find_element(By.XPATH, """//*[@id="form_pessoa"]/ul/li[2]/a""").click()

        endereco_cidade = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, '//*[@id="cidade"]/option[1]'))
        ).click()

        endereco_bairro = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "bairro"))
        ).send_keys(endereco["bairro"])
        
        endereco_numero = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "numero"))
        ).send_keys(endereco["numero"])
        
        endereco_cep = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "cep"))
        ).send_keys(endereco["cep"])

        endereco_referencia = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "referencia"))
        ).send_keys(endereco["referencia"])

        endereco_rua = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "rua"))
        ).send_keys(endereco["rua"])

        # adicionando contato
        contato_campo = driver.find_element(By.XPATH, """//*[@id="form_pessoa"]/ul/li[3]/a""").click()

        tipo_celular_opcao = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, '//*[@id="tipo"]/option[contains(text(), "CELULAR")]'))
        ).click()

        contato_telefone = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.ID, "fone"))
        ).send_keys(contato["telefone"])
        

        botao_salvar = driver.find_element(By.XPATH, """//*[@id="form_pessoa"]/input[2]""").click()

    # Verificar alerta de sucesso
        WebDriverWait(driver, 10).until(EC.alert_is_present())
        alert = driver.switch_to.alert

        if alert.text == "Pessoa salva com sucesso":
            print(alert.text)
            alert.accept()
        elif alert.text == "Já existe uma pessoa cadastrada com este CPF/CNPJ, verifique":
            print("Erro: CPF duplicado.")
            alert.accept()
            raise ValueError("CPF já cadastrado. Verifique os dados.")

    except UnexpectedAlertPresentException as e:
        alert = driver.switch_to.alert
        print(f"Alerta inesperado: {alert.text}")
        alert.accept()
        raise e

try:
    time.sleep(5)

    driver.get(MENU)

    login(driver)

    hover_lateral_menu(driver, "pessoas")

    criar_pessoa_botao = driver.find_element(By.XPATH, """//*[@id="btn-padrao"]/a""").click()

    cria_pessoa(
        nome = "Pedro",
        apelido = "Pedrosa",
        cpf = "11111111113",
        data_nascimento = "08/08/1998",
        observacao = "Cliente fiel",
        endereco = {
            "cidade": "Cacoal",
            "rua": "dos bobos",
            "bairro": "limoeiro",
            "numero": "0",
            "cep": 999999999,
            "referencia": "ali do lado"
        },
        contato = {
            "telefone": 99999999999,
            "tipo": "CELULAR"
        }
    )

    hover_lateral_menu(driver, "pessoas")

    
finally:
    driver.quit()