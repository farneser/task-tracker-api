def generate_jwt_secret(size: int = 32) -> str:
    import secrets

    return f"'{secrets.token_hex(size)}'".upper()


if __name__ == "__main__":
    print(f"Generated jwt secret key 32-byte (256-bit) size: {generate_jwt_secret()}")
