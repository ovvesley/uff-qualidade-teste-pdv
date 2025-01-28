from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.webdriver.common.action_chains import ActionChains

MENU = "http://localhost:8080/login"

def login(driver):
    barra_usuario = driver.find_element(By.ID, "user")
    barra_usuario.send_keys("gerente")

    barra_senha = driver.find_element(By.ID, "password")
    barra_senha.send_keys("123")

    botao = driver.find_element(By.ID, "btn-login")
    botao.click()

def hover_lateral_menu(driver, acao):
    
    if acao == "inicio":
        path_elemento_hover = "/html/body/div[3]/div/div[1]/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[1]/a"

    elif acao == "pedidos":
        path_elemento_hover = "/html/body/div[3]/div/div[2]/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[2]/a"
    
    elif acao =="pessoas":
        path_elemento_hover = "/html/body/div[3]/div/div[8]/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[8]/a"

    elif acao == "titulos":
        path_elemento_hover = "/html/body/div[3]/div/div[14]/ul/li/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[14]/ul/li/a"
        path_segundo_click = """//a[@class='opcoes' and @href='/titulos']"""

    elif acao == "maquina":
        path_elemento_hover = "/html/body/div[3]/div/div[14]/ul/li/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[14]/ul/li/a"
        path_segundo_click = """//a[@class='opcoes' and @href='/maquinacartao']"""

    elif acao == "produtos":
        path_elemento_hover = "/html/body/div[3]/div/div[10]/ul/li/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[10]/ul/li/a"
        path_segundo_click = """//*[@id="dashboard"]/li[1]/a"""

    elif acao == "estoque":
        path_elemento_hover = "/html/body/div[3]/div/div[10]/ul/li/a/img"
        path_elemento_click = "/html/body/div[3]/div/div[10]/ul/li/a"
        path_segundo_click = """//*[@id="dashboard"]/li[5]/a"""

    elemento_hover = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, path_elemento_hover))
    )

    actions = ActionChains(driver)
    actions.move_to_element(elemento_hover).perform()

    elemento_clicar = WebDriverWait(driver, 10).until(
        EC.element_to_be_clickable((By.XPATH, path_elemento_click))
    )
    elemento_clicar.click()

    lista_click_duplo = ["titulos", "maquina", "produtos", "estoque"]
    if acao in lista_click_duplo:
        segundo_elemento_clicar = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, path_segundo_click))
        )
        segundo_elemento_clicar.click()
    