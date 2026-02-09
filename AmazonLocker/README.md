# Amazon Locker

## Overview

Drivers deposit packages into lockers; the system assigns a compartment, issues a single one-time access token (with TTL) to the recipient, and the recipient uses that token to retrieve the package. Staff can manually access compartments with expired or problem packages.

## Requirements

Requirements:
1. Carrier deposits a package by specifying size (small, medium, large)
   - System assigns an available compartment of matching size
   - Opens compartment and returns access token, or error if no space
2. Upon successful deposit, an access token is generated and returned
   - One access token per package
3. User retrieves package by entering access token
   - System validates code and opens compartment
   - Throws specific error if code is invalid or expired
4. Access tokens expire after 7 days
   - Expired codes are rejected if used for pickup
   - Package remains in compartment until staff removes it
5. Staff can open all expired compartments to manually handle packages
   - System opens all compartments with expired tokens
   - Staff physically removes packages and returns them to sender
6. Invalid access tokens are rejected with clear error messages
   - Wrong code, already used, or expired - user gets specific feedback

Out of scope:
- How the package gets to the locker (delivery logistics)
- How the access token reaches the customer (SMS/email notification)
- Lockout after failed access token attempts
- UI/rendering layer
- Multiple locker stations
- Payment or pricing

## Entities

Locker:
    Fields:
        - compartments: Compartment[]
        - tokenToCompartmentMapping: HashMap<AccessToken, Compartment>
    Methods:
        + depositPackage(Size size) : String    // returns raw access code on success
        + withdrawPackage(String accessCode) : boolean // returns true on successful withdrawal
        + checkAvailable(Size size) : int // helper, returns compartment id or -1
        + removeExpiredPackages() : void
        - createToken(String accessCode, Instant timestamp, int compartmentId) : AccessToken

Compartment:
    Fields:
        - compartmentId: int
        - compartmentSize: Size
        - compartmentState: CompartmentState
    Methods:
        + getCompartmentId() : int
        + getCompartmentSize() : Size
        + getCompartmentState() : CompartmentState
        + setCompartmentState(state: CompartmentState) : void

AccessToken:
    Fields:
        - accessCode: String
        - expiryTimestamp: Instant
        - compartmentId: int
    Methods:
        + AccessToken(String accessCode, Instant expiryTimestamp, int compartmentId)
        + getAccessCode() : String
        + isExpired() : boolean
        + getCompartmentId() : int

enum Size: SMALL, MEDIUM, LARGE

enum CompartmentState: AVAILABLE, OCCUPIED, OUT_OF_SERVICE