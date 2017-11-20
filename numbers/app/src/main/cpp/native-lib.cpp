#include <jni.h>
#include <string>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

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
    int result;     // correct result
    int wresult1;   // wrong result
    int wresult2;   // wrong result
    int wresult3;   // wrong result
    int correct; // index [1..4] of the correct result
};

#define PRIORITY(_arg) (((_arg == Act_Multiply) || (_arg == Act_Divide)) ? 1:0)

static char ActSimbol[] = "+-*/";
static DiffLevel_t ActLevel = Lev_VeryBeginer;

int calculate(int num1, int &num2, MathAction_t act, bool bAdj)
{
    switch(act)
    {
        case Act_Plus:
            return num1 + num2;
        case Act_Minus:
            if (bAdj && (ActLevel < Lev_AllIncluded) && (num2 > num1))
                num2 = num1?rand()%num1:num1;
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

int getVisual1(TestInfo_t *info)
{
    if (info->correct == 1)
        return info->result;
    else
        return info->wresult1;
}
int getVisual2(TestInfo_t *info)
{
    if (info->correct == 2)
        return info->result;
    else if (info->correct == 1)
        return info->wresult1;
    else
        return info->wresult2;
}
int getVisual3(TestInfo_t *info)
{
    if (info->correct == 3)
        return info->result;
    else if (info->correct <= 2)
        return info->wresult2;
    else
        return info->wresult3;
}
int getVisual4(TestInfo_t *info)
{
    if (info->correct == 4)
        return info->result;
    else
        return info->wresult3;
}

bool calcStright(TestInfo_t *info, int* res = NULL)
{
    int tmp;

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
    int tmp;
    tmp = calculate(info->num2, info->num3, info->act2, res == NULL);
    if (!tmp && info->act1 == Act_Divide)
        return false;
    tmp = calculate(info->num1, tmp, info->act1, res == NULL);
    if (res)
        *res = tmp;
    else
        info->result = tmp;
    return true;
}

void adjust_wres(TestInfo_t *info)
{
    if (info->result == info->wresult1)
        info->wresult1 = calculate(info->result, info->num3?info->num3:info->num2?info->num2:info->num1, info->act2, false);
    if (info->result == info->wresult1)
        info->wresult1++;
    info->wresult2 = (info->result + info->wresult1) / 2;
    while (info->wresult2 == info->result || info->wresult2 == info->wresult1)
        info->wresult2++;
    info->wresult3 = info->result + (info->result + info->wresult1) / 2;
    while (info->wresult3 == info->result || info->wresult3 == info->wresult1 || info->wresult3 == info->wresult2)
        info->wresult3--;
}

void generate_test(TestInfo_t *info)
{
    static int num_of_calls = 0;
    static int initialized = 0;
    if (!initialized)
    {
        srand(time(NULL));
        initialized = 1;
    }
//    if (++num_of_calls > 25)
//        ActLevel = (DiffLevel_t)((int)ActLevel +1);
//    if (ActLevel > Lev_Iourka)
//        ActLevel = Lev_JustProgress;

    while (info)
    {
        memset(info, 0, sizeof(TestInfo_t));

        info->num1 = rand()%10;
        info->num2 = rand()%10;
        info->num3 = rand()%10;
        info->correct = rand()%4+1;

        int act_leveling = ActLevel == Lev_VeryBeginer ? Act_Multiply :
                           ActLevel == Lev_JustProgress ? Act_Divide :
                           ActLevel == Lev_PartsNoNegative ? Act_Max : Act_Max;

        info->act1 = (MathAction_t) (rand() % act_leveling);
        info->act2 = (MathAction_t) (rand() % act_leveling);

        if (PRIORITY(info->act1) >= PRIORITY(info->act2))
        {
            if (!calcStright(info))
                continue;
            // generate wrong result1 as opposite sequence
            if (!calcBack(info, &info->wresult1))
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
    sprintf(out, "%d", test.result);
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
    ActLevel = (DiffLevel_t)((int)ActLevel +1);
    if (ActLevel > Lev_Iourka)
        ActLevel = Lev_JustProgress;
    sprintf(out, "%d", (int) ActLevel);
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
    sprintf(out, "%d", (int) ActLevel);
    return env->NewStringUTF(out);
}

