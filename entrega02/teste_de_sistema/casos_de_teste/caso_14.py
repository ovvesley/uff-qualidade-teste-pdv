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

def criar_empresaEnota (nomeEmp, nomeFant, CNPJ, insc, rua, bairro, numero, cep, ref, nfe, aliq, natureza):
    time.sleep(5)

    botao_config = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[14]/ul/li/a/img').click()
    botao_empresa = driver.find_element(By.XPATH, '//*[@id="parametros"]/li[1]').click()
    time.sleep(5)
    nome_emp = driver.find_element(By.XPATH, '//*[@id="nome"]').send_keys(nomeEmp)
    nome_fant = driver.find_element(By.XPATH, '//*[@id="fantasia"]').send_keys(nomeFant)
    nome_cnpj = driver.find_element(By.XPATH, '//*[@id="cnpj"]').send_keys(CNPJ)
    insc = driver.find_element(By.XPATH, '//*[@id="ie"]').send_keys(insc)
    time.sleep(5)
    botao_end = driver.find_element(By.XPATH, '//*[@id="form_empresa"]/div/ul/li[2]/a').click()
    time.sleep(5)
    campo_Listacidade = driver.find_element(By.XPATH, '//*[@id="cidade"]').click()
    campo_cidade = driver.find_element(By.XPATH, '//*[@id="cidade"]/option[2]').click()

    campo_rua = driver.find_element(By.XPATH, '//*[@id="rua"]').send_keys(rua)
    campo_bairro = driver.find_element(By.XPATH, '//*[@id="bairro"]').send_keys(bairro)
    campo_numero = driver.find_element(By.XPATH, '//*[@id="numero"]').send_keys(numero)
    campo_cep = driver.find_element(By.XPATH, '//*[@id="cep"]').send_keys(cep)
    campo_ref = driver.find_element(By.XPATH, '//*[@id="ref"]').send_keys(ref)

    time.sleep(10)

    elemento = driver.find_element(By.XPATH, '//*[@id="form_empresa"]/div/ul/li[3]/a')
    driver.execute_script("arguments[0].scrollIntoView(true);", elemento)
    elemento.click()

    time.sleep(5)

    lista_regime = driver.find_element(By.XPATH, '//*[@id="rt"]').click()
    regime = driver.find_element(By.XPATH, '//*[@id="rt"]/option[2]').click()
    time.sleep(5)
    campo_nfe = driver.find_element(By.XPATH, '//*[@id="serie"]').send_keys(nfe)

    lista_ambiente = driver.find_element(By.XPATH, '//*[@id="ambiente"]').click()
    ambiente = driver.find_element(By.XPATH, '//*[@id="ambiente"]/option[2]').click()

    campo_ali = driver.find_element(By.XPATH, '//*[@id="pCredSN"]').send_keys(aliq)
    time.sleep(5)
    botao_salvar = driver.find_element(By.XPATH, '//*[@id="form_empresa"]/div/input').click()
    time.sleep(5)

    botao_nf= driver.find_element(By.XPATH, '/html/body/div[3]/div/div[12]/a/img').click()
    time.sleep(5)
    nova_nf = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a').click()
    time.sleep(5)
    campo_natureza = driver.find_element(By.XPATH, '//*[@id="natureza"]').send_keys(natureza)
    time.sleep(5)
    procura_lista = driver.find_element(By.XPATH, '//*[@id="destinatario"]/div[1]/div/div/div/div/div/button/span[1]').click()
    time.sleep(5)
    usuario = driver.find_element(By.XPATH, '//*[@id="destinatario"]/div[1]/div/div/div/div/div/div[2]/ul/li[2]/a/span[1]').click()
    time.sleep(5)
    criar = driver.find_element(By.XPATH, '/html/body/section[1]/div/div/a').click()
    time.sleep(5)
    

try:

    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    criar_empresaEnota(

    nomeEmp= "teste",
    nomeFant= "teste",
    CNPJ= '18.236.120/0001-58',
    insc="rj",
    rua="teste",
    bairro="teste",
    numero="20",
    cep="22222222",
    ref="qualqier",
    nfe="1",
    aliq="100",
    natureza="teste2"
)

finally:
    driver.quit()


