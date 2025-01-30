
# edita pessoa

from helpers.usuario import MENU, login, hover_lateral_menu

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.action_chains import ActionChains
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
import time
from selenium.common.exceptions import UnexpectedAlertPresentException


driver = webdriver.Chrome()

def editar_pessoa(nome=None, apelido=None, cpf=None, data_nascimento=None, observacao=None, endereco=None, contato=None):
    try:

        if nome:
            campo_nome = driver.find_element(By.ID, "nome")
            campo_nome.clear()
            campo_nome.send_keys(nome)
        
        if apelido:
            campo_apelido = driver.find_element(By.ID, "apelido")
            campo_apelido.clear()
            campo_apelido.send_keys(apelido)
        
        if cpf:
            campo_cpf = driver.find_element(By.ID, "cpfcnpj")
            campo_cpf.clear()
            campo_cpf.send_keys(cpf)
        
        if data_nascimento:
            campo_data_nascimento = driver.find_element(By.ID, "nascimento")
            campo_data_nascimento.clear()
            campo_data_nascimento.send_keys(data_nascimento)
        
        if observacao:
            campo_observacao = driver.find_element(By.ID, "observacao")
            campo_observacao.clear()
            campo_observacao.send_keys(observacao)
        
        if endereco:
            driver.find_element(By.XPATH, """//*[@id="form_pessoa"]/ul/li[2]/a""").click()
            
            if "cidade" in endereco:
                WebDriverWait(driver, 10).until(
                    EC.element_to_be_clickable((By.XPATH, f'//*[@id="cidade"]/option[contains(text(), "{endereco["cidade"]}")]'))
                ).click()
            
            if "bairro" in endereco:
                campo_bairro = driver.find_element(By.ID, "bairro")
                campo_bairro.clear()
                campo_bairro.send_keys(endereco["bairro"])
            
            if "numero" in endereco:
                campo_numero = driver.find_element(By.ID, "numero")
                campo_numero.clear()
                campo_numero.send_keys(endereco["numero"])
            
            if "cep" in endereco:
                campo_cep = driver.find_element(By.ID, "cep")
                campo_cep.clear()
                campo_cep.send_keys(endereco["cep"])
            
            if "referencia" in endereco:
                campo_referencia = driver.find_element(By.ID, "referencia")
                campo_referencia.clear()
                campo_referencia.send_keys(endereco["referencia"])
            
            if "rua" in endereco:
                campo_rua = driver.find_element(By.ID, "rua")
                campo_rua.clear()
                campo_rua.send_keys(endereco["rua"])

        if contato:
            driver.find_element(By.XPATH, """//*[@id="form_pessoa"]/ul/li[3]/a""").click()
            
            if "tipo" in contato:
                WebDriverWait(driver, 10).until(
                    EC.element_to_be_clickable((By.XPATH, f'//*[@id="tipo"]/option[contains(text(), "{contato["tipo"]}")]'))
                ).click()
            
            if "telefone" in contato:
                campo_telefone = driver.find_element(By.ID, "fone")
                campo_telefone.clear()
                campo_telefone.send_keys(contato["telefone"])

        driver.find_element(By.XPATH, """//*[@id="form_pessoa"]/input[2]""").click()

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

    time.sleep(2)

    login(driver)

    hover_lateral_menu(driver, "pessoas")

    driver.find_element(By.XPATH, "/html/body/section[2]/div/table/tbody/tr[2]/td[6]").click()

    editar_pessoa(
        apelido = "jonas"
    )

finally:
    driver.quit()