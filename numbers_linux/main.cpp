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
static DiffLevel_t ActLevel = Lev_AllIncluded;//Lev_VeryBeginer;

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
    {
        if ((info->act1 == info->act2) && (info->act1 == Act_Multiply))
            info->wresult1 = info->num1 * info->num3;
        else
            info->wresult1 = calculate(info->result, info->num3?info->num3:info->num2?info->num2:info->num1, info->act2, false);
    }
    if (info->result == info->wresult1)
        info->wresult1 += rand()%9;
    info->wresult2 = (info->result + info->wresult1) / 2 + info->num2;
    while (info->wresult2 == info->result || info->wresult2 == info->wresult1)
        info->wresult2++;
    info->wresult3 = info->wresult2 + (info->result + info->wresult1) / 2;
    while (info->wresult3 == info->result || info->wresult3 == info->wresult1 || info->wresult3 == info->wresult2)
        info->wresult3 -= rand()%9;
}

void generate_test(TestInfo_t *info)
{
    static int initialized = 0;
    if (!initialized)
    {
        srand(time(NULL));
        initialized = 1;
    }

    while (info)
    {
        memset(info, 0, sizeof(TestInfo_t));

        while ((!info->num1 && !info->num2) ||
               (!info->num1 && !info->num3) ||
               (!info->num2 && !info->num3))
        {
            info->num1 = rand()%10;
            info->num2 = rand()%10;
            info->num3 = rand()%10;
        }

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

typedef int (*getVisual) (TestInfo_t *info);
bool validate (TestInfo_t *info)
{
    getVisual val[4] = {getVisual1,getVisual2,getVisual3,getVisual4};
    if ((*(val[info->correct-1]))(info) == info->result)
        return true;
    return false;
}

void printout_test(TestInfo_t *info)
{
    printf("%d %c %d %c %d = [%d], place %d visual: [%d] | [%d] | [%d] | [%d] - %s\n", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3,
            info->result, info->correct, getVisual1(info), getVisual2(info), getVisual3(info), getVisual4(info), validate(info)?"ok":"-------------oopos!!!-----------");
    if (!validate(info))
        perror("oops");
}

void printout_quest(TestInfo_t *info)
{
    printf("%d %c %d %c %d = ", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3);
}

void sprint_quest(char *out, TestInfo_t *info)
{
    sprintf(out, "%d %c %d %c %d = ", info->num1, ActSimbol[info->act1], info->num2, ActSimbol[info->act2], info->num3);
}


int main(int argc, char *argv[])
{
    (void) argc;
    (void) argv;

    srand(time(NULL));

    TestInfo_t test;

#if !DEBUG_GEN
        for (int i = 0; i < 200; i++)
        {
            generate_test(&test);
            printout_test(&test);
            if ((i+1)%10 == 0)
                printf("-----------------------------------\n");
        }
#else
    do {
        int result;
        char input[16];

        generate_test(&test);
        printout_quest(&test);

        scanf("%s", input);

        if (strstr(input, "quit"))
        {
            break;
        }
        else if (1 == sscanf(input, "%d", &result))
        {
            if (result == test.result)
                printf("ok, %d\n", test.result);
            else
                printf("oopos... nope, it's %d\n", test.result);
        }
    } while (1);
#endif
    return 0;
}
