#!/bin/bash 

# Verification script for environment setup (AI Generated)
# Run this before pushing to git

echo "🔍 Verifying environment setup..."
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

ERRORS=0

# Check .gitignore
echo "📋 Checking .gitignore..."
if git check-ignore .env >/dev/null 2>&1; then
    echo -e "${GREEN}✅ .env is ignored${NC}"
else
    echo -e "${RED}❌ .env is NOT ignored!${NC}"
    ERRORS=$((ERRORS + 1))
fi

if git check-ignore firebase-service-account.json >/dev/null 2>&1; then
    echo -e "${GREEN}✅ firebase-service-account.json is ignored${NC}"
else
    echo -e "${RED}❌ firebase-service-account.json is NOT ignored!${NC}"
    ERRORS=$((ERRORS + 1))
fi

if git check-ignore notification-frontend/public/firebase-messaging-sw.js >/dev/null 2>&1; then
    echo -e "${GREEN}✅ firebase-messaging-sw.js is ignored${NC}"
else
    echo -e "${RED}❌ firebase-messaging-sw.js is NOT ignored!${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo ""

# Check .env.example files exist
echo "📄 Checking .env.example files..."
if [ -f "notification-frontend/.env.example" ]; then
    echo -e "${GREEN}✅ Frontend .env.example exists${NC}"
else
    echo -e "${RED}❌ Frontend .env.example missing!${NC}"
    ERRORS=$((ERRORS + 1))
fi

if [ -f "notification-backend/.env.example" ]; then
    echo -e "${GREEN}✅ Backend .env.example exists${NC}"
else
    echo -e "${RED}❌ Backend .env.example missing!${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo ""

# Check .env files exist (but not in git)
echo "🔐 Checking .env files..."
if [ -f "notification-frontend/.env" ]; then
    echo -e "${GREEN}✅ Frontend .env exists${NC}"
    if git ls-files --error-unmatch notification-frontend/.env >/dev/null 2>&1; then
        echo -e "${RED}❌ Frontend .env is tracked by git!${NC}"
        ERRORS=$((ERRORS + 1))
    else
        echo -e "${GREEN}✅ Frontend .env is NOT tracked by git${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Frontend .env missing (needed for local dev)${NC}"
fi

if [ -f "notification-backend/.env" ]; then
    echo -e "${GREEN}✅ Backend .env exists${NC}"
    if git ls-files --error-unmatch notification-backend/.env >/dev/null 2>&1; then
        echo -e "${RED}❌ Backend .env is tracked by git!${NC}"
        ERRORS=$((ERRORS + 1))
    else
        echo -e "${GREEN}✅ Backend .env is NOT tracked by git${NC}"
    fi
else
    echo -e "${YELLOW}⚠️  Backend .env missing (needed for local dev)${NC}"
fi

echo ""

# Check for hardcoded secrets
echo "🔎 Checking for hardcoded secrets..."
if git diff --cached | grep -q "AIzaSy"; then
    echo -e "${RED}❌ Found potential API key in staged changes!${NC}"
    ERRORS=$((ERRORS + 1))
else
    echo -e "${GREEN}✅ No API keys found in staged changes${NC}"
fi

echo ""

# Check template files
echo "📝 Checking template files..."
if [ -f "notification-frontend/public/firebase-messaging-sw.template.js" ]; then
    echo -e "${GREEN}✅ Service worker template exists${NC}"
else
    echo -e "${RED}❌ Service worker template missing!${NC}"
    ERRORS=$((ERRORS + 1))
fi

if [ -f "notification-frontend/generate-sw.js" ]; then
    echo -e "${GREEN}✅ Service worker generator exists${NC}"
else
    echo -e "${RED}❌ Service worker generator missing!${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo ""

# Check source code uses env vars
echo "💻 Checking source code..."
if grep -q "import.meta.env.VITE_" notification-frontend/src/config/firebase.js; then
    echo -e "${GREEN}✅ Frontend uses environment variables${NC}"
else
    echo -e "${RED}❌ Frontend doesn't use environment variables!${NC}"
    ERRORS=$((ERRORS + 1))
fi

if grep -q "\${" notification-backend/src/main/resources/application.properties; then
    echo -e "${GREEN}✅ Backend uses environment variables${NC}"
else
    echo -e "${RED}❌ Backend doesn't use environment variables!${NC}"
    ERRORS=$((ERRORS + 1))
fi

echo ""
echo "================================"

if [ $ERRORS -eq 0 ]; then
    echo -e "${GREEN}✅ All checks passed! Ready to push to git!${NC}"
    echo ""
    echo "Next steps:"
    echo "  git add ."
    echo "  git commit -m \"Add Firebase push notifications with environment variables\""
    echo "  git push origin main"
    exit 0
else
    echo -e "${RED}❌ Found $ERRORS error(s). Please fix before pushing!${NC}"
    echo ""
    echo "See GIT_PUSH_CHECKLIST.md for help"
    exit 1
fi
