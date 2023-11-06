def generate_jwt_secret(size: int = 32) -> str:
    import secrets

    return secrets.token_hex(size)


if __name__ == "__main__":
    print(f"Generated jwt secret key 32 byte size: ${generate_jwt_secret()}")
