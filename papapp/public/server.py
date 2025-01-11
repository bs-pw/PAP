from http.server import SimpleHTTPRequestHandler, HTTPServer


class CustomHandler(SimpleHTTPRequestHandler):
    def do_GET(self):
        # Jeśli żądany plik nie istnieje, przekieruj do index.html
        if not self.path.endswith(".html") and not self.path.startswith("/static"):
            self.path = "/index.html"
        return super().do_GET()


if __name__ == "__main__":
    PORT = 8000
    server = HTTPServer(("0.0.0.0", PORT), CustomHandler)
    print(f"Serwer HTTP działa na porcie {PORT}")
    server.serve_forever()
