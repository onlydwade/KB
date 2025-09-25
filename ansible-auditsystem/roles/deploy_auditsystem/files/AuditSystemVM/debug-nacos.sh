#!/bin/bash

# Simple test script to debug Nacos upload
echo "Testing direct Nacos upload..."

# Test 1: Try to add a simple property to the existing config
echo "=== Test 1: Simple Property Addition ==="
response=$(curl -s -X POST -u "nacos:nacos" \
    "http://localhost:8848/nacos/v1/cs/configs" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "dataId=test-config" \
    --data-urlencode "group=DEFAULT_GROUP" \
    --data-urlencode "content=test: value" \
    --data-urlencode "type=yaml")

echo "Response: '$response'"

# Test 2: Check if the test config was created
echo -e "\n=== Test 2: Verify Test Config ==="
verify_response=$(curl -s -u "nacos:nacos" \
    "http://localhost:8848/nacos/v1/cs/configs?dataId=test-config&group=DEFAULT_GROUP")

echo "Verify Response: '$verify_response'"

# Test 3: Try to update existing application-local.yml with just one small change
echo -e "\n=== Test 3: Update Existing Config ==="

# Get current content
current=$(curl -s -u "nacos:nacos" \
    "http://localhost:8848/nacos/v1/cs/configs?dataId=application-local.yml&group=DEFAULT_GROUP")

echo "Current config length: $(echo "$current" | wc -c)"

# Add a simple comment to the end
new_content="$current
# TEST MODIFICATION $(date)"

echo "New config length: $(echo "$new_content" | wc -c)"

update_response=$(curl -s -X POST -u "nacos:nacos" \
    "http://localhost:8848/nacos/v1/cs/configs" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    --data-urlencode "dataId=application-local.yml" \
    --data-urlencode "group=DEFAULT_GROUP" \
    --data-urlencode "content=$new_content" \
    --data-urlencode "type=yaml")

echo "Update response: '$update_response'"

# Test 4: Verify the update
echo -e "\n=== Test 4: Verify Update ==="
sleep 2
verify_update=$(curl -s -u "nacos:nacos" \
    "http://localhost:8848/nacos/v1/cs/configs?dataId=application-local.yml&group=DEFAULT_GROUP")

echo "Updated config length: $(echo "$verify_update" | wc -c)"
echo "Last few lines:"
echo "$verify_update" | tail -5

echo -e "\n=== Test Complete ==="
