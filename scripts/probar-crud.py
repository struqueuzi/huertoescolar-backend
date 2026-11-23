"""Prueba HTTP real. Crea y elimina solamente sus propios registros ficticios."""
import base64
import json
import os
import urllib.error
import urllib.request

clave = os.environ["HUERTO_API_CLAVE"]
token = base64.b64encode(("docente:" + clave).encode()).decode()
def request(method, url, payload=None, authenticated=True):
    headers = {"Content-Type": "application/json"}
    if authenticated:
        headers["Authorization"] = "Basic " + token
    req = urllib.request.Request(url, json.dumps(payload).encode() if payload is not None else None, headers, method=method)
    try:
        with urllib.request.build_opener(urllib.request.ProxyHandler({})).open(req, timeout=5) as res:
            raw = res.read()
            return res.status, json.loads(raw) if raw else None
    except urllib.error.HTTPError as err:
        return err.code, None

for port, route, value, edit in [(8081,"insumos",{"cantidad":3},{"cantidad":7}),(8082,"jornadas",{"fecha":"2026-11-25"},{"fecha":"2026-11-26"})]:
    url = f"http://127.0.0.1:{port}/api/{route}"
    assert request("GET",url,authenticated=False)[0] == 401
    assert request("POST",url,{"nombre":"",**value})[0] == 400
    code, item = request("POST",url,{"nombre":"Prueba de evaluación",**value})
    assert code == 201
    detail = url + "/" + str(item["id"])
    try:
        assert any(row["id"] == item["id"] for row in request("GET",url)[1])
        assert request("GET",detail)[1] == item
        code, updated = request("PUT",detail,{"nombre":"Prueba editada",**edit})
        assert code == 200 and all(updated[k]==v for k,v in edit.items())
    finally:
        assert request("DELETE",detail)[0] == 204
    assert request("GET",detail)[0] == 404
    print(f"{route}: autenticación, validación, alta, listado, detalle, edición y baja correctos")
