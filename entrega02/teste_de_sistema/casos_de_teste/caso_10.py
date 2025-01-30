#Lançar despesa, confirma que a despesa foi lançada

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

def lancar_despesa(valor, vencimento, obs):
    time.sleep(5)

    campo_despesa = driver.find_element(By.XPATH, '/html/body/div[3]/div/div[6]/a/img').click()

    time.sleep(5)
    botao_novo = driver.find_element(By.XPATH, '//*[@id="btn-padrao"]/a').click()

    time.sleep(5)
    campo_ListaFornecedor = driver.find_element(By.XPATH, '//*[@id="codFornecedor"]').click()

    time.sleep(5)

    campo_fornecedor = driver.find_element(By.XPATH, '//*[@id="codFornecedor"]/option[1]').click()

    time.sleep(5)

    campo_valor = driver.find_element(By.XPATH, '//*[@id="vltotalDespesa"]').send_keys(valor)
    time.sleep(5)

    campo_ListaDespesa = driver.find_element(By.XPATH, '//*[@id="despesatipo"]').click()
    time.sleep(5)
    campo_despesa = driver.find_element(By.XPATH, '//*[@id="despesatipo"]/option[1]').click()

    time.sleep(5)
    campo_vencimento = driver.find_element(By.XPATH, '//*[@id="dataVencimento"]').send_keys(vencimento)

    time.sleep(5)
    campo_obs = driver.find_element(By.XPATH, '//*[@id="obs"]').send_keys(obs)

    time.sleep(5)
    campo_lancar = driver.find_element(By.XPATH, '/html/body/section[2]/div/div/div[3]/div/div/div/div/form/div[6]/a').click()

    time.sleep(5)


    try:

        WebDriverWait(driver, 5).until(EC.alert_is_present())  
        alert = driver.switch_to.alert 
        print(f"Texto do alerta: {alert.text}") 
        alert.accept() 
        assert True, "despesa foi lançada com sucesso"

    except TimeoutException:
        print("Nenhum alerta foi encontrado.")
        assert False, "Erro: despesa não pode ser lançada"




try:

    time.sleep(5)

    
    driver.get(MENU)

    time.sleep(2)

    login(driver)

    lancar_despesa(

    valor = 100,
    vencimento = "03/02/2025",
    obs = "teste"
)

    lancar_despesa(

        valor = 100,
        vencimento="03/12/2025",
        obs="teste2"
    )


finally:
    driver.quit()



