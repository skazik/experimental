#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <math.h>

enum DiffLevel_t
{
    Lev_VeryBeginer,    // + and - no negative
    Lev_JustProgress,   // + , - and * no negative
    Lev_PartsNoNegative,// all no negative
    Lev_AllIncluded,    // all and negative
    Lev_Iourka,
};

enum MathAction_t
{
    Act_Plus = 0,
    Act_Minus,
    Act_Multiply,
    Act_Divide,
    Act_Max
};

struct TestInfo_t
{
    int num1;
    int num2;
    int num3;
    MathAction_t act1;
    MathAction_t act2;
    float result;     // correct result
    int wresult1;   // wrong result
    int wresult2;   // wrong result
    int wresult3;   // wrong result
    int correct; // index [1..4] of the correct result
};

#define PRIORITY(_arg) (((_arg == Act_Multiply) || (_arg == Act_Divide)) ? 1:0)

static int giAddOnly = 0;
static char ActSimbol[] = "+-*/";
static DiffLevel_t ActLevel = Lev_VeryBeginer;
static DiffLevel_t DisplayLevel = Lev_VeryBeginer;

float calculate(float num1, int &num2, MathAction_t act, bool bAdj)
{
    switch(act)
    {
        case Act_Plus:
            return num1 + num2;
        case Act_Minus:
            if (bAdj && (ActLevel < Lev_AllIncluded) && (num2 > num1))
            num2 = num1?rand()%(int)num1:num1;
            return num1 - num2;
        case Act_Multiply:
            return num1 * num2;
        case Act_Divide:
            if (bAdj)
                num2 = num2 != 0 ? num2 : rand()%9+1;
            else if (num2 == 0)
                return num1;
            return num1 / num2;
        default:
            break;
    }
    return 0;
}
float calculate(float num1, float num2, MathAction_t act, bool bAdj)
{
    if (bAdj)
        perror("not adjustable num2!!!");
    int intnum2 = (int) num2;
    return calculate(num1, intnum2, act, false);
}

int getVisual1(TestInfo_t *info)
{
    if (info->correct == 1)
        return round(info->result);
    else
        return info->wresult1;
}
int getVisual2(TestInfo_t *info)
{
    if (info->correct == 2)
        return round(info->result);
    else if (info->correct == 1)
        return info->wresult1;
    else
        return info->wresult2;
}
int getVisual3(TestInfo_t *info)
{
    if (info->correct == 3)
        return round(info->result);
    else if (info->correct <= 2)
        return info->wresult2;
    else
        return info->wresult3;
}
int getVisual4(TestInfo_t *info)
{
    if (info->correct == 4)
        return round(info->result);
    else
        return info->wresult3;
}

bool calcStright(TestInfo_t *info, int* res = NULL)
{
    float tmp;

    tmp = calculate(info->num1, info->num2, info->act1, res == NULL);
    tmp = calculate(tmp, info->num3, info->act2, res == NULL);
    if (res)
        *res = tmp;
    else
        info->result = tmp;
    return true;
}

bool calcBack(TestInfo_t *info, int* res = 0)
{
    float tmp;
    tmp = calculate(info->num2, info->num3, info->act2, res == NULL);
    if (!tmp && info->act1 == Act_Divide)
        return false;
    tmp = calculate(info->num1, tmp, info->act1, false);
    if (res)
        *res = (int) tmp;
    else
        info->result = tmp;
    return true;
}

void adjust_wres(TestInfo_t *info)
{
    if (round(info->result) == info->wresult1)
    {
        if ((info->act1 == info->act2) && (info->act1 == Act_Multiply))
            info->wresult1 = info->num1 * info->num3;
        else
            info->wresult1 = calculate(round(info->result), info->num3?info->num3:info->num2?info->num2:info->num1, info->act2, false);
    }
    if (round(info->result) == info->wresult1)
        info->wresult1 += rand()%9;
    info->wresult2 = (round(info->result) + info->wresult1) / 2 + info->num2;
    while (info->wresult2 == round(info->result) || info->wresult2 == info->wresult1)
        info->wresult2++;
    info->wresult3 = info->wresult2 + (round(info->result) + info->wresult1) / 2;
    while (info->wresult3 == round(info->result) || info->wresult3 == info->wresult1 || info->wresult3 == info->wresult2)
        info->wresult3 -= rand()%9;
}

bool check_no_more_than_one_zero(TestInfo_t *info)
{
    if (info->num1 == 0) {
        if (info->num2 == 0 || info->num3 == 0)
            return false;
    } else if (info->num2 == 0 && info->num3 == 0) {
        return false;
    }
    return true;
}

void generate_test(TestInfo_t *info, bool bGenerate = true)
{
    static int initialized = 0;
    if (!initialized)
    {
        srand(time(NULL));
        initialized = 1;
    }

    while (info)
    {
        if (bGenerate)
        {
            memset(info, 0, sizeof(TestInfo_t));

            while (check_no_more_than_one_zero(info) == false)
            {
                info->num1 = rand()%10;
                info->num2 = rand()%10;
                info->num3 = rand()%10;
            }
        }
        info->correct = rand()%4+1;

        int act_leveling = ActLevel == Lev_VeryBeginer ? Act_Multiply :
                           ActLevel == Lev_JustProgress ? Act_Divide :
                           ActLevel == Lev_PartsNoNegative ? Act_Max : Act_Max;

        if (bGenerate)
        {
            if (giAddOnly) {
                info->act1 = info->act2 = Act_Plus;
            }
            else {
                info->act1 = (MathAction_t) (rand() % act_leveling);
                info->act2 = (MathAction_t) (rand() % act_leveling);
            }
        }

        if (PRIORITY(info->act1) >= PRIORITY(info->act2))
        {
            if (!calcStright(info))
                continue;
            // generate wrong result1 as opposite sequence
            if (!calcBack(info, &info->wresult1))
                continue;

            if (!check_no_more_than_one_zero(info))
                continue;

            adjust_wres(info);
        }
        else
        {
            if (!calcBack(info))
                continue;
            // generate wrong result1 as opposite sequence
            if (!calcStright(info, &info->wresult1))
                continue;

            if (!check_no_more_than_one_zero(info))
                continue;

            adjust_wres(info);
        }
        break;
    }
}

void sprint_test(char *out, TestInfo_t *info)
{
    sprintf(out, "%d %c %d %c %d = %d\n", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3, info->result);
}

void sprint_quest(char *out, TestInfo_t *info)
{
    sprintf(out, "%d %c %d %c %d = ", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3);
}

static TestInfo_t test;

extern "C"
JNIEXPORT void

JNICALL
Java_com_example_learnupp_numbers_MainActivity_setAddOnly(
        JNIEnv *env,
        jobject /* this */) {
    giAddOnly = 1;
}

extern "C"
JNIEXPORT void

JNICALL
Java_com_example_learnupp_numbers_MainActivity_unsetAddOnly(
        JNIEnv *env,
        jobject /* this */) {
    giAddOnly = 0;
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    generate_test(&test);
    sprint_quest(out, &test);
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_resultFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    int res = (int) round(test.result);
    sprintf(out, "%d", res);
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_get1valFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    sprintf(out, "%d", getVisual1(&test));
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_get2valFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    sprintf(out, "%d", getVisual2(&test));
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_get3valFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    sprintf(out, "%d", getVisual3(&test));
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_get4valFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    sprintf(out, "%d", getVisual4(&test));
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_levelUpFromJNI(
        JNIEnv *env,
    jobject /* this */) {
    char out[64];
    if (ActLevel < Lev_Iourka)
        ActLevel = (DiffLevel_t)((int)ActLevel +1);
    DisplayLevel = (DiffLevel_t)((int)DisplayLevel +1);
    sprintf(out, "%d", (int) DisplayLevel);
    std::string output = out;
    return env->NewStringUTF(output.c_str());
}

extern "C"
JNIEXPORT jstring

JNICALL
Java_com_example_learnupp_numbers_MainActivity_getLevelFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    char out[64];
    sprintf(out, "%d", (int) DisplayLevel);
    return env->NewStringUTF(out);
}

extern "C"
JNIEXPORT int

JNICALL
Java_com_example_learnupp_numbers_MainActivity_getNum1FromJNI(
        JNIEnv *env,
        jobject /* this */) {
    return test.num1;
}
extern "C"
JNIEXPORT int

JNICALL
Java_com_example_learnupp_numbers_MainActivity_getNum2FromJNI(
        JNIEnv *env,
        jobject /* this */) {
    return test.num2;
}
extern "C"
JNIEXPORT int

JNICALL
Java_com_example_learnupp_numbers_MainActivity_getNum3FromJNI(
        JNIEnv *env,
        jobject /* this */) {
    return test.num3;
}

