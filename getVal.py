import pip._vendor.requests as requests

resp = requests.get("https://developer.nrel.gov/api/alt-fuel-stations/v1/nearest.json?api_key=7cHeCIVy4MqgJTz1VfCNneQlby0FUiYyvPXqVSSY&location=Denver+CO")
if resp.status_code != 200:
    raise APIError('GET /tasks/ {}'.format(resp.status_code))
for to in resp.json():
    print(to)

