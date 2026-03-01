#!/bin/bash

# Base API URL
API_URL="http://localhost:8080/api/v1"

echo "========================================="
echo "      CREATING CLASS SCHEDULES           "
echo "========================================="
# Assuming the IDs generated for the above resources map sequentially 1 to 5.

echo "Creating Class Schedule 1: Math on Monday..."
curl --location "$API_URL/class-schedules" \
--header 'Content-Type: application/json' \
--data-raw '{
    "teacherId": 1,
    "studentId": 1,
    "subjectId": 1,
    "dayOfWeek": "MONDAY",
    "startTime": "10:00:00",
    "durationMinutes": 60,
    "meetLink": "https://meet.google.com/aaa-bbbb-ccc"
}'
echo -e "\n"

echo "Creating Class Schedule 2: Physics on Tuesday..."
curl --location "$API_URL/class-schedules" \
--header 'Content-Type: application/json' \
--data-raw '{
    "teacherId": 2,
    "studentId": 2,
    "subjectId": 2,
    "dayOfWeek": "TUESDAY",
    "startTime": "14:00:00",
    "durationMinutes": 90,
    "meetLink": "https://meet.google.com/ddd-eeee-fff"
}'
echo -e "\n"

echo "Creating Class Schedule 3: CompSci on Wednesday..."
curl --location "$API_URL/class-schedules" \
--header 'Content-Type: application/json' \
--data-raw '{
    "teacherId": 3,
    "studentId": 3,
    "subjectId": 3,
    "dayOfWeek": "WEDNESDAY",
    "startTime": "09:00:00",
    "durationMinutes": 60,
    "meetLink": "https://meet.google.com/ggg-hhhh-iii"
}'
echo -e "\n"

echo "Creating Class Schedule 4: English on Thursday..."
curl --location "$API_URL/class-schedules" \
--header 'Content-Type: application/json' \
--data-raw '{
    "teacherId": 4,
    "studentId": 4,
    "subjectId": 4,
    "dayOfWeek": "THURSDAY",
    "startTime": "11:00:00",
    "durationMinutes": 60,
    "meetLink": "https://meet.google.com/jjj-kkkk-lll"
}'
echo -e "\n"

echo "Creating Class Schedule 5: History on Friday..."
curl --location "$API_URL/class-schedules" \
--header 'Content-Type: application/json' \
--data-raw '{
    "teacherId": 5,
    "studentId": 5,
    "subjectId": 5,
    "dayOfWeek": "FRIDAY",
    "startTime": "13:30:00",
    "durationMinutes": 120,
    "meetLink": "https://meet.google.com/mmm-nnnn-ooo"
}'
echo -e "\n"

echo "Finished seeding Class Schedules."